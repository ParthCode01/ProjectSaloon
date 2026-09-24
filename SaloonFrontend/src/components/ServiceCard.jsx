function ServiceCard({ service, onSelect }) {
    return (
        <div className="service-card">
            <h3>{service.name}</h3>
            <p>₹{service.price}</p>

            <button onClick={() => onSelect(service)}>
                Select
            </button>
        </div>
    );
}

export default ServiceCard;
