using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.Repository
{
    public interface ICarMaintenanceRepository
    {
        void Save(SaveCarMaintenanceRecord saveRecord);

        int Count();
    }
}