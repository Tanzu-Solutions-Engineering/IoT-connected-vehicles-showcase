using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ML.Data;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain
{
    public class CarMaintenance
    {
         /*
        slno,vehicle_type,brand,model,engine_type,bonnet,door,front_bumper,rear_bumper,front_headlight,back_headlight,side_mirror,tail_light,clutch_plate,barke_pad,front_windsheild,rear_windsheild,ac_evaporator_coil,radiator,battery,total_acc
        */
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
        public float oil_filter { get; set; }
        [LoadColumn(10)]
        public float engine_oil { get; set; }
        [LoadColumn(11)]
        public float washer_plug_drain { get; set; }
        [LoadColumn(12)]
        public float dust_and_pollen_filter { get; set; }
        [LoadColumn(13)]
        public float whell_alignment_and_balancing { get; set; }

        [LoadColumn(14)]
        public float air_clean_filter { get; set;}

        [LoadColumn(15)]
        public float fuel_filter { get; set;}

        [LoadColumn(16)]
        public float spark_plug { get; set;}

        [LoadColumn(17)]
        public float brake_fluid { get; set;}

        [LoadColumn(18)]
        public float brake_and_clutch_oil { get; set;}

        [LoadColumn(19)]
        public float transmission_fluid { get; set;}

        [LoadColumn(20)]
        public float brake_pads { get; set;}

        [LoadColumn(21)]
        public float clutch { get; set;}

        [LoadColumn(22)]
        public float coolant  { get; set;}

        [LoadColumn(23)]
        public float cost  { get; set;}

        [LoadColumn(24)]
        public bool label { get; set; }
        
    }
}