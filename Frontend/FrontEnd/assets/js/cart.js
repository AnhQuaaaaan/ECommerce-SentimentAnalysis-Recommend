document.addEventListener("DOMContentLoaded", () => {
    updateCartCount()
})
function openCart() {
    renderCart()
    document.querySelector('.modal-cart').classList.add('open');
}

function closeCart() {
    document.querySelector('.modal-cart').classList.remove('open');
}
async function updateCartCount() {
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;

    if (!userId) {
        return;
    }
    try {
        const response = await fetch(`http://localhost:8080/api/cartItems/customer/${userId}`);
        if (!response.ok) {
            console.error('Lỗi khi lấy dữ liệu giỏ hàng:', await response.text());
            return;
        }

        const cart = await response.json();
        const totalQuantity = cart.reduce((sum, item) => sum + item.quantity, 0);

        const countElement = document.querySelector('.count-product-cart');
        if (countElement) {
            countElement.textContent = totalQuantity;
        }
    } catch (error) {
        console.error('Lỗi khi gọi API để cập nhật số lượng:', error);
    }
}

async function renderCart() {
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;

    if (!userId) {
        return;
    }

    const cartList = document.querySelector('.cart-list');
    const cartEmptyMessage = document.querySelector('.gio-hang-trong');
    const cartTotalPrice = document.querySelector('.cart-total-price .text-price');

    try {
        const response = await fetch(`http://localhost:8080/api/cartItems/customer/${userId}`);
        if (!response.ok) {
            console.error('Không thể lấy dữ liệu giỏ hàng:', await response.text());
            return;
        }

        const cartItems = await response.json();

        if (cartItems.length === 0) {
            cartList.innerHTML = '';
            cartEmptyMessage.style.display = 'flex';
            cartTotalPrice.textContent = '0.00$';
            const checkoutButton = document.querySelector('.thanh-toan');
            if (checkoutButton) {
                checkoutButton.disabled = true;
            }
        } else {
            cartEmptyMessage.style.display = 'none';
            const checkoutButton = document.querySelector('.thanh-toan');
            if (checkoutButton) {
                checkoutButton.disabled = false;
            }
            cartList.innerHTML = '';

            let totalPrice = 0;

            cartItems.forEach(item => {
                const listItem = document.createElement('li');
                listItem.classList.add('cart-item');

                const product = item.product;

                listItem.innerHTML = `
                    <div class="cart-item-info">
                        <div class="cart-item-img-container">
                            <img src="${product.image}" class="cart-item-img">
                        </div>
                        <div class="cart-item-details">
                            <p class="cart-item-name">${product.name}</p>
                            <p class="cart-item-price">${product.price}$</p>
                            <div class="cart-item-quantity">
                                <button class="quantity-btn-minus" onclick="decreaseQuantity('${item.id}','${item.quantity}')">-</button>
                                <span id="quantity-${item.id}" class="quantity">${item.quantity}</span>
                                <button class="quantity-btn-plus" onclick="increaseQuantity('${item.id}','${item.quantity}')">+</button>
                                <button class="cart-item-remove" onclick="removeItemFromCart('${item.id}')">
                                    <i class="fa fa-trash"></i> Xóa
                                </button>
                            </div>
                        </div>
                    </div>
                `;

                cartList.appendChild(listItem);

                totalPrice += product.price * item.quantity;
            });

            cartTotalPrice.textContent = totalPrice.toFixed(2) + '$';
        }
    } catch (error) {
        console.error('Lỗi khi gọi API:', error);
    }
}


async function removeItemFromCart(itemId) {
    try {
        const response = await fetch(`http://localhost:8080/api/cartItems/${itemId}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            console.error('Lỗi khi xóa mục khỏi giỏ hàng:', await response.text());
            return;
        }
        updateCartCount();
        renderCart();
    } catch (error) {
        console.error('Lỗi khi gọi API xóa mục:', error);
    }
}

async function increaseQuantity(itemId, quantity1) {
    let a = parseInt(quantity1, 10) + 1;
    try {
        const response = await fetch('http://localhost:8080/api/cartItems', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                id: itemId,
                quantity: a
            })
        });

        if (!response.ok) {
            console.error('Failed to update quantity:', await response.text());
            return;
        }
        const quantityElement = document.querySelector(`#quantity-${itemId}`);
        if (quantityElement) {
            quantityElement.textContent = a;
        }
        renderCart();
        updateTotalPrice();
    } catch (error) {
        console.error('Error while updating quantity:', error);
    }
}

async function decreaseQuantity(itemId, quantity1) {
    let currentQuantity = parseInt(quantity1, 10);
    if (currentQuantity <= 1) {
        console.log("khong được bé hơn không")
        return;
    }

    let newQuantity = currentQuantity - 1;
    try {
        const response = await fetch('http://localhost:8080/api/cartItems', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                id: itemId,
                quantity: newQuantity
            })
        });

        if (!response.ok) {
            console.error('Failed to update quantity:', await response.text());
            return;
        }


        const quantityElement = document.querySelector(`#quantity-${itemId}`);
        if (quantityElement) {
            quantityElement.textContent = newQuantity;
        }
        renderCart();
        updateTotalPrice();
    } catch (error) {
        console.error('Error while updating quantity:', error);
    }
}

async function updateTotalPrice() {
    try {
        const response = await fetch('http://localhost:8080/api/cartItems/customer/A9MHZQO27LL00V');
        if (!response.ok) {
            console.error('Lỗi khi lấy dữ liệu giỏ hàng:', await response.text());
            return;
        }

        const cart = await response.json();
        const totalPrice = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

        const roundedTotalPrice = totalPrice.toFixed(2);

        const totalPriceElement = document.querySelector('.cart-total-price .text-price');
        if (totalPriceElement) {
            totalPriceElement.textContent = roundedTotalPrice + '$';
        }

        updateCartCount();
    } catch (error) {
        console.error('Lỗi khi cập nhật tổng giá:', error);
    }
}

function thanhtoan() {
    window.location.href = "order.html"
}