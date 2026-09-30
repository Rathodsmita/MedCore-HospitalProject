package com.medcore;

record BookingRequest(String name, String phone, String department, String doctor, String date) {}

record BookingResponse(String reference, String name, String doctor, String date, String status) {}

record StatusUpdate(String status) {}
