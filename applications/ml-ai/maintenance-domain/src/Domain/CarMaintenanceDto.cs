using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace  Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain
{
    public class CarMaintenanceDto
    {

        public CarMaintenanceDto(){
        }

        public CarMaintenanceDto(string vin, CarMaintenance carMaintenance)
        {
            this.vin = vin;
            this.carMaintenance = carMaintenance;
        }

        public string? vin{ get; set; } 
        public CarMaintenance? carMaintenance { get; set;}

        public override string ToString()
        {
            return $"CarMaintenanceDto vin: {vin}, CarMaintenance : {carMaintenance}";
        }

    }
}