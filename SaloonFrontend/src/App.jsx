import { BrowserRouter, Routes, Route } from "react-router-dom";

import HomePage from "./pages/HomePage";
import Navbar from "./components/Navbar.jsx";
import SalonDetails from "./pages/SalonDetails";

function App() {
    return (
        <BrowserRouter>

            <Navbar />

            <Routes>

                <Route
                    path="/"
                    element={<HomePage />}
                />

                <Route
                    path="/salon/:id"
                    element={<SalonDetails />}
                />

            </Routes>
        </BrowserRouter>
    );
}

export default App;