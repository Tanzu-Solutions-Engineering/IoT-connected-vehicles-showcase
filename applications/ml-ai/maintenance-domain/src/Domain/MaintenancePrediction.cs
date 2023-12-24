using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ML.Data;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain
{
    public class MaintenancePrediction
    {
         // ColumnName attribute is used to change the column name from
        // its default value, which is the name of the field.
        [ColumnName("PredictedLabel")]
        public bool Prediction;

        // No need to specify ColumnName attribute, because the field
        // name "Probability" is the column name we want.
        public float Probability;

        public float Score;

        public override string ToString(){
            return $"MaintenancePrediction  Prediction: {Prediction},  Probability : {Probability}, Score : {Score}";
        }  
        
    }
}