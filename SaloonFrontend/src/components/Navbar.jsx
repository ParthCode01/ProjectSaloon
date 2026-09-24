import { Link } from "react-router-dom";

function Navbar() {
    return (
        <nav className="navbar">
            <Link to="/" className="navbar-logo">
                SalonIQ
            </Link>

            <div className="navbar-links">
                <Link to="/">Home</Link>
            </div>
        </nav>
    );
}

export default Navbar;