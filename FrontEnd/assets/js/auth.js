async function handleLogin(event) {
    event.preventDefault();

    const usernameInput = document.getElementById('usernamelogin');
    const passwordInput = document.getElementById('passwordlogin');
    let err = document.getElementById('password-error');
    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    if (!username || !password) {
        err.textContent = "Tài khoản hoặc mật khẩu không được bỏ trống";
        err.style.visibility = 'visible';
        return;
    }

    const data = {
        username: username,
        password: password
    };

    try {
        const response = await fetch('http://localhost:8080/api/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const errorText = await response.text();
            err.textContent = errorText
            err.style.visibility = 'visible';
            return;
        }
        const result = await response.json();
        localStorage.setItem('token', result.accessToken);
        localStorage.setItem('user', JSON.stringify(result.user));
        window.location.href = '/FrontEnd/index.html';
    } catch (error) {
        console.log(error)
    }
}

async function handleRegister(event) {
    event.preventDefault();

    const username = document.getElementById('usernameregister').value.trim();
    const password = document.getElementById('passwordregister').value.trim();
    const fullname = document.getElementById('fullname').value;
    const email = document.getElementById('email').value;
    const dob = document.getElementById('dob').value;
    const address = document.getElementById('address').value;
    const phone = document.getElementById('phone').value;
    let err = document.getElementById('password-error');

    if (!username || !password) {
        err.textContent = "Tài khoản hoặc mật khẩu không được bỏ trống";
        err.style.visibility = 'visible';
        return;
    }

    const data = {
        username: username,
        password: password,
        fullname: fullname,
        email: email,
        dob: dob,
        address: address,
        phone: phone
    };

    try {
        const response = await fetch('http://localhost:8080/api/user/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorText = await response.text();
            err.textContent = errorText
            err.style.visibility = 'visible';
            return;
        }
        window.location.href = '/FrontEnd/login.html';
    } catch (error) {
        console.log(error)
    }
}
