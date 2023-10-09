package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Gregory Green
 */
@RestController
class VehicleStreamingController(
    private val vehicleRepository: VehicleRepository,
    private val objectMapper: ObjectMapper = jacksonObjectMapper()) {

    @CrossOrigin
    @RequestMapping(value = ["/updates"])
    fun updates(response: HttpServletResponse) {
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.contentType = "text/event-stream"
        response.setHeader("Cache-Control", "no-cache")
        response.characterEncoding = "UTF-8"

        var writer = response.writer;

        val vehicles = vehicleRepository.findAll();
        for (vehicle in vehicles)
        {
                writer.println("data: ${objectMapper.writeValueAsString(vehicle)}\r\n");

        }
        writer.flush();
    }

}