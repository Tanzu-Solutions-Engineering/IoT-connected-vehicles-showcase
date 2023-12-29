using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.src.Repository;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.Repository
{
    public class CarMaintenanceRepository : ICarMaintenanceRepository
    {
        private ILogger<CarMaintenanceRepository> logger;
        private CarMaintenanceDbContext dbContext;

        public CarMaintenanceRepository(ILogger<CarMaintenanceRepository> logger, CarMaintenanceDbContext dbContext)
        {
            this.logger = logger;
            this.dbContext = dbContext;
        }

        public int Count()
        {
            return dbContext.CarMaintenances.Count();
        }

        public void Save(SaveCarMaintenanceRecord saveRecord)
        {
            if(saveRecord.carMaintenanceCount > 0)
            {
                for(int i=0;i< saveRecord.carMaintenanceCount;i++)
                {
                    dbContext.Add(saveRecord.carMaintenance);
                }
            }
            else
            {
                //save once
                dbContext.Add(saveRecord.carMaintenance);
            }
            
            dbContext.SaveChanges();
        }
    }
}