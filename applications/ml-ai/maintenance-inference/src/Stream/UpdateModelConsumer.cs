using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ML;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Stream;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.src.Stream
{
    public class UpdateModelConsumer
    {
        private MaintenanceProcessor processor;
        private readonly MLContext mLContext;

        public UpdateModelConsumer(MaintenanceProcessor processor)
        {
            mLContext = new MLContext();
            this.processor = processor;
        }

        internal void UpdateModel(byte[] model)
        {
            DataViewSchema modelSchema;

            MemoryStream stream = new MemoryStream(model);

            ITransformer newModel = mLContext.Model.Load(stream,out modelSchema);
            processor.TrainedModel = newModel;
        }
    }
}