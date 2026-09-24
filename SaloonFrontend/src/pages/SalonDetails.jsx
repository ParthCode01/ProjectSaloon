import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import BookingForm from "../components/BookingForm.jsx";
import ServiceCard from "../components/ServiceCard.jsx";
import {
    getTenantById,
    getTreatmentsByTenantId
} from "../services/api.js";

function SalonDetails() {

    const { id } = useParams();

    const [salon, setSalon] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [treatments , setTreatments] = useState([]);
    const [treatmentsError, setTreatmentsError] = useState("");
    const [selectedService, setSelectedService] = useState(null);
    const [showBookingForm, setShowBookingForm] = useState(false);

    useEffect(() => {
        const fetchSalon = async () => {
            try {
                const response = await getTenantById(id);
                if (response.data) {
                    setSalon(response.data);
                } else {
                    setError("Salon not found");
                }
            } catch (error) {
                setError("Failed to load salon.");
            } finally {
                setLoading(false);
            }
        };

        fetchSalon();
    }, [id]);

    useEffect(() => {
        const fetchTreatments = async () => {
            try {
                const response = await getTreatmentsByTenantId(id);
                setTreatments(response.data);
            } catch (error) {
                setTreatmentsError("Failed to load services.");
            }
        };

        fetchTreatments();
    }, [id]);


    if (loading) {
        return <p>Loading salon...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    if (!salon) {
        return <p>Salon not found</p>;
    }

    return (
        <div className="salon-details">
            <h1>{salon.name}</h1>
            <p>{salon.address}</p>


            <h2>Services</h2>

            {treatmentsError && <p>{treatmentsError}</p>}

            {treatments.length === 0 ? (
                <p>No services available for this salon.</p>
            ) : (
                treatments.map(treatment => (
                <ServiceCard
                    key={treatment.id}
                    service={treatment}
                    onSelect={(treatment) => {
                        setSelectedService(treatment);
                        setShowBookingForm(true);
                    }}
                />
            ))
            )}

            {selectedService && (
                <div>
                    <h2>Selected Service</h2>
                    <p>{selectedService.name}</p>
                    <p>₹{selectedService.price}</p>

                    <button onClick={() => setShowBookingForm(true)}>
                        Book Appointment
                    </button>

                    {showBookingForm && (
                        <BookingForm selectedService={selectedService} />
                    )}
                </div>
            )}
        </div>
    );
}

export default SalonDetails;