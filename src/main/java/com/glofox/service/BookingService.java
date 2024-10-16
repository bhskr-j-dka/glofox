package com.glofox.service;

import com.glofox.model.Booking;
import com.glofox.model.Class;
import com.glofox.repository.BookingRepository;
import com.glofox.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ClassRepository classRepository;

    public Booking createBooking(Booking newBooking) {
    	validateBooking(newBooking);
        return bookingRepository.save(newBooking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
    	
    	validateBooking(updatedBooking);
    	
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        existingBooking.setClassId(updatedBooking.getClassId());
        existingBooking.setDate(updatedBooking.getDate());
        existingBooking.setName(updatedBooking.getName());

        return bookingRepository.save(existingBooking);
    }



    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    private void validateBooking(Booking booking) {
        if (booking.getName() == null || booking.getName().isEmpty()) {
            throw new RuntimeException("Booking name cannot be empty.");
        }
        if (booking.getDate() == null) {
            throw new RuntimeException("Booking date cannot be null.");
        }

        // Check if the associated class exists and validate the booking date range
        Class bookedClass = classRepository.findById(booking.getClassId())
                .orElseThrow(() -> new RuntimeException("Invalid Class ID. Please provide a valid class ID."));

        if (booking.getDate().isBefore(bookedClass.getStartDate()) || booking.getDate().isAfter(bookedClass.getEndDate())) {
            throw new RuntimeException("Booking date must be in the range of the class start date and end date.");
        }
    }
}
