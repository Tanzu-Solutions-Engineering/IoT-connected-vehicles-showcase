using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.Repository;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.src.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class CarMaintenanceController 
    {
        private readonly ILogger<CarMaintenanceController> logger;
        private ICarMaintenanceRepository repository;

        public CarMaintenanceController(ICarMaintenanceRepository repository,ILogger<CarMaintenanceController> logger)
        {
            this.repository = repository;
            this.logger = logger;
        }

        [HttpPost]
        public void SaveCarMaintenance(SaveCarMaintenanceRecord saveRecord)
        {
            Console.WriteLine($"Saving saveRecord: {saveRecord}");
            this.logger.LogInformation($"Saving record: {saveRecord}");
            this.repository.Save(saveRecord);
        }
    }
}