using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ML.Data;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.ML
{
    public interface ITrainer
    {
        byte[] Train();
    }

}