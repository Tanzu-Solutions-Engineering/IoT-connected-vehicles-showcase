using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ML;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction
{
    public interface IPredictor
    {
        ITransformer? TrainedModel { get; set;}

        public MaintenanceDto Predict(CarMaintenanceDto carMaintenanceDto);

        
    }
}