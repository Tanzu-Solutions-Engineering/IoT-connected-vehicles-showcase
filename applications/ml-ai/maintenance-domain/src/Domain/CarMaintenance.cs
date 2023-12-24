using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ML.Data;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain
{
    public class CarMaintenance
    {
        
          [LoadColumn(0)]
        public int slno { get; set; }

        [LoadColumn(1)]
        public float vehicle_type { get; set; }
        [LoadColumn(2)]
        public float brand { get; set; }
        [LoadColumn(3)]
        public float model { get; set; }
        [LoadColumn(4)]
        public float engine_type { get; set; }
        [LoadColumn(5)]
        public float make_year { get; set; }
        [LoadColumn(6)]
        public float region { get; set; }
        [LoadColumn(7)]
        public float mileage_range { get; set; }
        [LoadColumn(8)]
        public float mileage { get; set; }
  
        [LoadColumn(9)]
        public bool label { get; set; }

        public override string ToString()
        {
            return $"CarMaintenance slno:{slno} vehicle_type:{this.vehicle_type} brand:{this.brand} model:{this.model} engine_type:{this.engine_type} make_year:{this.make_year} region:{this.region} mileage_range:{this.mileage_range} mileage:{this.mileage} label:{this.label}";
        }
        
    }
}