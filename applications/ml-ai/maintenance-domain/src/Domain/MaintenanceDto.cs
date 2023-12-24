using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain
{
    public class MaintenanceDto
    {
        public string? vin { get; set;}

        public MaintenancePrediction? prediction { get; set; }

        public override string ToString(){
            return $"MaintenanceDto  vin: {vin},  prediction : {prediction}";
        }  
    }
}