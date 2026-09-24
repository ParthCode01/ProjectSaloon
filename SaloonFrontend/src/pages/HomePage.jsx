import SalonCard from "../components/SalonCard";
import {useEffect, useState} from "react";

import {getTenants} from "../services/api.js";

function HomePage() {

    const [search, setSearch] = useState("");
    const [salons, setSalons] = useState([]);

    const [error , setError] = useState("");
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        const fetchSalons = async () => {
            try {
                const response = await getTenants();
                setSalons(response.data);
            } catch (error) {
                setError("Failed to load salons.");
            } finally {
                setLoading(false);
            }
        };

        fetchSalons();
    }, []);

    if (error) {
        return <p>{error}</p>;
    }
    if (loading) {
        return <p>Loading salons...</p>;
    }

    const filteredSalons = salons.filter(salon =>
        salon.name.toLowerCase().includes(search.toLowerCase()) ||
        salon.address.toLowerCase().includes(search.toLowerCase())
    );

    if (filteredSalons.length === 0) {
        return <p>No salons found.</p>;
    }

    return (
        <div className="home-page">


            <h1>SalonIQ</h1>

            <h2>Find Your Perfect Salon</h2>

            <input
                className="search-input"
                type="text"
                placeholder="Search salons..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
            />

            <h2>Popular Salons</h2>

            <div className="salon-grid">
                {filteredSalons.map(salon => (
                    <SalonCard
                        key={salon.id}
                        salon={salon}
                    />
                ))}
            </div>

        </div>
    );
}

export default HomePage;