function closecheckout() {
    window.location.href = "index.html"
}
document.addEventListener("DOMContentLoaded", () => {
    loadOrder();
    displayDeliveryDate();
    loadUsser()
})
let token = localStorage.getItem("token");
function displayDeliveryDate() {
    const today = new Date();

    today.setDate(today.getDate() + 2);

    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const year = today.getFullYear();

    const deliveryDate = `${day}/${month}/${year}`;

    const dateOrderElement = document.querySelector(".date-order");
    dateOrderElement.textContent = `Hàng sẽ được giao trước ngày ${deliveryDate}.`;
}
async function loadOrder() {
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;

    if (!userId) {
        console.error('User ID không tồn tại trong localStorage.');
        return;
    }
    const listOrderCheckout = document.getElementById("list-order-checkout");
    const totalBillOrder = document.querySelector(".total-bill-order");
    const totalPriceElement = document.getElementById("checkout-cart-price-final");

    let totalPrice = 0;
    const shippingFee = 10;
    let totalQuantity = 0;

    try {
        const response = await fetch(`http://localhost:8080/api/cartItems/customer/${userId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) {
            console.error('Failed to fetch cart items:', await response.text());
            return;
        }

        const orders = await response.json();

        listOrderCheckout.innerHTML = "";

        orders.forEach(order => {
            const orderItem = document.createElement("div");
            orderItem.classList.add("order-item");

            orderItem.innerHTML = `
                <img src="${order.product.image}" >
                <div class="info">
                    <h4>${order.product.name}</h4>
                    <div class="or">
                        <div class="priceor">${order.price} $</div>
                        <div class="quantityor">${order.quantity}X</div>
                    </div>
                </div>
            `;

            listOrderCheckout.appendChild(orderItem);
            totalPrice += order.price * order.quantity;
            totalQuantity += order.quantity;
        });

        totalPriceElement.textContent = totalPrice.toLocaleString() + " $";

        const totalWithShipping = totalPrice + shippingFee;

        totalBillOrder.innerHTML = `
            <div class="bill-row">
                <span>Tiền hàng (<span class="text-red-small">${totalQuantity} món</span>):</span>
                <span class="text-right">${totalPrice.toLocaleString()} $</span>
            </div>
            <div class="bill-row">
                <span>Phí vận chuyển:</span>
                <span class="text-right">${shippingFee.toLocaleString()} $</span>
            </div>
        `;

        totalPriceElement.textContent = totalWithShipping.toLocaleString() + " $";
    } catch (error) {
        console.error('Error while fetching cart data:', error);
    }
}

function loadUsser() {
    const userInfo = JSON.parse(localStorage.getItem('user')) || {};
    if (userInfo) {
        document.getElementById('tennguoinhan').value = userInfo.fullname || '';
        document.getElementById('sdtnhan').value = userInfo.phone || '';
        document.getElementById('emailnguoinhan').value = userInfo.email || '';
        document.getElementById('diachinhan').value = userInfo.address || '';
    }
}
function showPopup(message) {
    document.getElementById("popup-message").textContent = message;
    document.getElementById("popup").style.display = "flex";
}

function closePopup() {
    document.getElementById("popup").style.display = "none";
    window.location.href = "index.html"
}
let orderID
async function ordersp() {
    await createOrder();
    saveOrderItems();
}
async function createOrder() {
    const userInfo = JSON.parse(localStorage.getItem('user'));
    const totalPriceElement = document.getElementById('checkout-cart-price-final');

    const customerName = document.getElementById('tennguoinhan').value || '';
    const email = document.getElementById('emailnguoinhan').value || '';
    const customerPhone = document.getElementById('sdtnhan').value || '';
    const deliveryAddress = document.getElementById('diachinhan').value || '';
    const note = document.querySelector('.note-order').value || '';

    const customerId = userInfo.id;
    const totalAmount = parseFloat(totalPriceElement.innerText) || 0;
    const orderDate = new Date().toISOString().split('T')[0];

    const orderData = {
        customerName,
        customerPhone,
        email,
        deliveryAddress,
        note,
        orderDate,
        totalAmount,
        customer: {
            id: customerId
        }
    };
    try {
        const response = await fetch('http://localhost:8080/api/order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(orderData)
        });

        if (!response.ok) {
            throw new Error('Failed to create order: ' + response.statusText);
        }

        showPopup("Đặt hàng thành công!");
    } catch (error) {
        console.error('Error creating order:', error);
        showPopup("Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
    }
}
async function sendProductDataToBackend() {
    const userInfo = JSON.parse(localStorage.getItem('user')) || {};
    const cart = JSON.parse(localStorage.getItem('cart')) || [];

    if (!userInfo.email) {
        console.error("Không tìm thấy email của người dùng.");
        return;
    }
    const productData = cart.map(item => ({
        id: item.id,
        name: item.name,
        image: item.image
    }));

    const payload = {
        email: userInfo.email,
        products: productData
    };

    try {
        const response = await fetch("http://localhost:8080/api/order/send-product-data", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            console.log("Dữ liệu sản phẩm đã được gửi thành công.");
        } else {
            console.error("Gửi dữ liệu thất bại:", response.statusText);
        }
    } catch (error) {
        console.error("Lỗi khi gửi dữ liệu:", error);
    }
}