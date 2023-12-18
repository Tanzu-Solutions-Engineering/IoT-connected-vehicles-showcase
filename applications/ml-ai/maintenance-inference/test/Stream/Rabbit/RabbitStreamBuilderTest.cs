using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Imani.Solutions.Core.API.Util;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using RabbitMQ.Client;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream.Rabbit;
using Steeltoe.Messaging.RabbitMQ.Support;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.test.Stream.Rabbit
{
    [TestClass]
    public class RabbitStreamBuilderTest
    {

   
        
        [TestMethod]
        public void toCarMaintenanceDto()
        {
            byte[] jsonBytes = Encoding.UTF8.GetBytes(@"
            { 
                ""vin"" : ""001"",
                ""carMaintenance"" : {
                    ""slno"" : 1,
                    ""vehicle_type"" : 1,
                    ""brand"" : 1,
                    ""model"" : 1,
                    ""engine_type"" : 1,
                    ""make_year"" : 1,
                    ""region"" : 1,
                    ""mileage_range"" : 1,
                    ""label"" : false
                }
            }");

            var actual  = RabbitStreamBuilder.toCarMaintenanceDto(jsonBytes);

            Assert.AreEqual("001",actual.vin);
        }
    }
}