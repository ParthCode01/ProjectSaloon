import { useState } from "react";

function Login() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    function handleSubmit(event) {
        event.preventDefault();

        console.log("Email:", email);
        console.log("Password:", password);
    }

    return (
        <form onSubmit={handleSubmit}>

            <h2>SalonIQ Login</h2>

            <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
            />

            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
            />

            <button type="submit">
                Login
            </button>

        </form>
    );
}

export default Login;