using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain
{
    public record SaveCarMaintenanceRecord(CarMaintenance carMaintenance, 
    int carMaintenanceCount)
    {        
    }
}