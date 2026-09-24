import { useState } from "react";

function BookingForm({ selectedService }) {

    const [bookingDate, setBookingDate] = useState("");
    const [bookingTime, setBookingTime] = useState("");

    const handleBooking = () => {
        if (!bookingDate || !bookingTime) {
            alert("Please select date and time");
            return;
        }

        alert(
            `Booking confirmed for ${selectedService.name} on ${bookingDate} at ${bookingTime}`
        );
    };

    return (
        <div className="booking-form">
            <h2>Book Appointment</h2>

            <label>
                Date:
                <input
                    type="date"
                    value={bookingDate}
                    onChange={(e) => setBookingDate(e.target.value)}
                />
            </label>

            <br />

            <label>
                Time:
                <input
                    type="time"
                    value={bookingTime}
                    onChange={(e) => setBookingTime(e.target.value)}
                />
            </label>

            <br />

            <button onClick={handleBooking}>
                Confirm Booking
            </button>
        </div>
    );
}

export default BookingForm;