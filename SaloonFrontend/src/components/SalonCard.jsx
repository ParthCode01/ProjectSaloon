import { useNavigate } from "react-router-dom";

function SalonCard({ salon }) {

    const navigate = useNavigate();

    return (
        <div className="salon-card">
            <h2>{salon.name}</h2>
            <p>{salon.address}</p>

            <button onClick={() => navigate(`/salon/${salon.id}`)}>
                View Salon
            </button>
        </div>
    );
}

export default SalonCard;