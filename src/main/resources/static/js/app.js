// ========================================================
// Smart Canteen Token System - Swiggy & Zomato App Logic
// ========================================================

// --- Cart Helpers ---
function getCart() {
    try {
        const cart = localStorage.getItem('cart');
        return cart ? JSON.parse(cart) : [];
    } catch (e) {
        return [];
    }
}

function saveCart(cart) {
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartBadge();
    syncMenuCardButtons();
}

function updateCartBadge() {
    const cart = getCart();
    const count = cart.reduce((sum, item) => sum + item.quantity, 0);
    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);

    // Navbar Badge
    const badge = document.getElementById('cart-badge');
    if (badge) {
        badge.textContent = count;
        badge.style.display = count > 0 ? 'inline-block' : 'none';
    }

    // Swiggy Floating Bottom Bar
    const floatBar = document.getElementById('floating-cart');
    const floatCount = document.getElementById('float-count');
    const floatTotal = document.getElementById('float-total');

    if (floatBar) {
        if (count > 0) {
            if (floatCount) floatCount.textContent = count + (count === 1 ? ' ITEM' : ' ITEMS');
            if (floatTotal) floatTotal.textContent = '₹' + total.toFixed(2);
            floatBar.classList.add('visible');
        } else {
            floatBar.classList.remove('visible');
        }
    }
}

// --- Menu Page: Swiggy Add & Stepper Sync ---
function syncMenuCardButtons() {
    const cart = getCart();
    document.querySelectorAll('.swiggy-food-card').forEach(card => {
        const id = parseInt(card.dataset.id);
        const cartItem = cart.find(i => i.foodItemId === id);
        const addBtn = card.querySelector('.swiggy-add-btn');
        const stepper = card.querySelector('.stepper-mini');
        const qtyInput = card.querySelector('.stepper-qty');

        if (addBtn && stepper && qtyInput) {
            if (cartItem && cartItem.quantity > 0) {
                addBtn.style.display = 'none';
                stepper.style.display = 'flex';
                qtyInput.value = cartItem.quantity;
            } else {
                addBtn.style.display = 'block';
                stepper.style.display = 'none';
                qtyInput.value = 1;
            }
        }
    });
}

function addFoodItem(btn) {
    const card = btn.closest('.swiggy-food-card');
    const id = parseInt(card.dataset.id);
    const name = card.dataset.name;
    const price = parseFloat(card.dataset.price);
    const image = card.dataset.image || '/images/placeholder.jpg';

    let cart = getCart();
    const existing = cart.find(item => item.foodItemId === id);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ foodItemId: id, name: name, price: price, image: image, quantity: 1 });
    }
    saveCart(cart);
    showToast('Added ' + name + ' to cart! 🍽️');
}

function modifyCardQty(btn, delta) {
    const card = btn.closest('.swiggy-food-card');
    const id = parseInt(card.dataset.id);

    let cart = getCart();
    const itemIndex = cart.findIndex(i => i.foodItemId === id);
    if (itemIndex > -1) {
        cart[itemIndex].quantity += delta;
        if (cart[itemIndex].quantity <= 0) {
            cart.splice(itemIndex, 1);
        }
        saveCart(cart);
    }
}

// --- Cart Page: Render Swiggy Split Layout ---
function renderCart() {
    const cart = getCart();
    const emptyEl = document.getElementById('cart-empty');
    const contentEl = document.getElementById('cart-content');
    const itemsContainer = document.getElementById('cart-items-container');
    const billSubtotal = document.getElementById('bill-subtotal');
    const billTotal = document.getElementById('bill-total');

    if (!emptyEl || !contentEl) return;

    if (cart.length === 0) {
        emptyEl.style.display = 'block';
        contentEl.style.display = 'none';
        updateCartBadge();
        return;
    }

    emptyEl.style.display = 'none';
    contentEl.style.display = 'grid';

    let html = '';
    let total = 0;

    cart.forEach((item, index) => {
        const subtotal = item.price * item.quantity;
        total += subtotal;
        html += `
            <div class="cart-row">
                <div class="cart-row-item-info">
                    <img src="${item.image}" alt="${item.name}"
                         onerror="this.onerror=null;this.src='/images/placeholder.jpg'"
                         class="cart-item-thumb">
                    <div>
                        <div class="cart-item-name">${item.name}</div>
                        <div class="cart-item-unit-price">₹${item.price.toFixed(2)} each</div>
                    </div>
                </div>

                <div class="cart-row-stepper">
                    <div class="stepper-mini">
                        <button class="stepper-btn" onclick="updateCartRowQty(${index}, -1)">−</button>
                        <input type="text" class="stepper-qty" value="${item.quantity}" readonly>
                        <button class="stepper-btn" onclick="updateCartRowQty(${index}, 1)">+</button>
                    </div>
                </div>

                <div class="cart-row-price">
                    ₹${subtotal.toFixed(2)}
                </div>

                <button onclick="removeCartRow(${index})" style="background:none; border:none; color:#e53935; cursor:pointer; font-size:1.1rem; padding:4px;" title="Remove">
                    ✕
                </button>
            </div>
        `;
    });

    if (itemsContainer) itemsContainer.innerHTML = html;
    if (billSubtotal) billSubtotal.textContent = '₹' + total.toFixed(2);
    if (billTotal) billTotal.textContent = '₹' + total.toFixed(2);
    updateCartBadge();
}

function updateCartRowQty(index, delta) {
    let cart = getCart();
    if (cart[index]) {
        cart[index].quantity += delta;
        if (cart[index].quantity <= 0) {
            cart.splice(index, 1);
        }
        saveCart(cart);
        renderCart();
    }
}

function removeCartRow(index) {
    let cart = getCart();
    cart.splice(index, 1);
    saveCart(cart);
    renderCart();
}

// --- Place Order ---
function placeOrder() {
    const cart = getCart();
    if (cart.length === 0) {
        showToast('Your cart is empty!', true);
        return;
    }

    const payload = cart.map(item => ({
        foodItemId: item.foodItemId,
        quantity: item.quantity
    }));

    const csrfToken = document.querySelector('meta[name="_csrf"]');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]');

    const headers = { 'Content-Type': 'application/json' };
    if (csrfToken && csrfHeader) {
        headers[csrfHeader.content] = csrfToken.content;
    }

    fetch('/order/place', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => { throw new Error(data.error || 'Failed to place order'); });
        }
        return response.json();
    })
    .then(data => {
        localStorage.removeItem('cart');
        window.location.href = '/order/confirmation/' + data.orderId;
    })
    .catch(error => {
        showToast(error.message, true);
    });
}

// --- Order Status Polling & Notifications ---
function pollOrderStatuses() {
    const orderCards = document.querySelectorAll('.order-card[data-status="PLACED"]');
    if (orderCards.length === 0) return;

    orderCards.forEach(card => {
        const orderId = card.dataset.orderId;
        const tokenNo = card.dataset.token;
        pollSingleOrder(orderId, tokenNo, card);
    });
}

function pollSingleOrder(orderId, tokenNo, card) {
    setInterval(() => {
        fetch('/api/orders/' + orderId + '/status')
            .then(response => response.json())
            .then(data => {
                if (data.status === 'READY' && card.dataset.status !== 'READY') {
                    card.dataset.status = 'READY';
                    const badge = card.querySelector('.status-badge');
                    if (badge) {
                        badge.textContent = 'READY';
                        badge.className = 'status-badge status-ready';
                    }
                    showOrderReadyPopup(tokenNo);
                    playBeep();
                    showToast('🎉 Your Order is Done - Token No. ' + tokenNo);
                } else if (data.status === 'COLLECTED' && card.dataset.status !== 'COLLECTED') {
                    card.dataset.status = 'COLLECTED';
                    const badge = card.querySelector('.status-badge');
                    if (badge) {
                        badge.textContent = 'COLLECTED';
                        badge.className = 'status-badge status-collected';
                    }
                }
            })
            .catch(() => {});
    }, 5000);
}

function showOrderReadyPopup(tokenNo) {
    const popup = document.getElementById('order-ready-popup');
    const tokenEl = document.getElementById('popup-token-no');
    if (popup && tokenEl) {
        tokenEl.textContent = tokenNo;
        popup.style.display = 'flex';
    }
}

function closePopup() {
    const popup = document.getElementById('order-ready-popup');
    if (popup) {
        popup.style.display = 'none';
    }
}

// --- Audio Tone ---
function playBeep() {
    try {
        const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
        const oscillator = audioCtx.createOscillator();
        const gainNode = audioCtx.createGain();
        oscillator.connect(gainNode);
        gainNode.connect(audioCtx.destination);
        oscillator.frequency.value = 880;
        oscillator.type = 'sine';
        gainNode.gain.setValueAtTime(0.3, audioCtx.currentTime);
        gainNode.gain.exponentialRampToValueAtTime(0.001, audioCtx.currentTime + 0.6);
        oscillator.start(audioCtx.currentTime);
        oscillator.stop(audioCtx.currentTime + 0.6);
    } catch (e) {}
}

// --- Toast ---
function showToast(message, isError) {
    const toast = document.getElementById('toast');
    if (!toast) return;
    toast.textContent = message;
    toast.style.background = isError ? '#e53935' : 'linear-gradient(135deg, #1f2a5c, #0a1128)';
    toast.classList.add('show');
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3200);
}

document.addEventListener('DOMContentLoaded', function() {
    updateCartBadge();
});
