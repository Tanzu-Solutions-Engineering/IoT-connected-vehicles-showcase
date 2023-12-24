using System.Data;
using Microsoft.Extensions.Logging;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.Repository;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.test.Repository
{
    [TestClass]
    public class TrainingDataLoaderTest
    {
        private TrainingDataLoader subject;
        private Mock<IDbConnection> connection;
        private Mock<IDbCommand> command;
        private Mock<IDataReader> reader;
        private int tableRecordCount = 40;
        private Mock<ILogger<TrainingDataLoader>> logger;

        [TestMethod]
        public void Load()
        {
            connection = new Mock<IDbConnection>();
            command = new Mock<IDbCommand>();
            reader = new Mock<IDataReader>();
            logger = new Mock<ILogger<TrainingDataLoader>> ();

            connection.Setup(s => s.CreateCommand()).Returns(command.Object);
            command.Setup( c => c.ExecuteReader()).Returns(reader.Object);
            reader.Setup( r => r.Read()).Returns(true);
            reader.Setup( r => r.GetInt32(It.IsAny<int>())).Returns(tableRecordCount);

            subject = new TrainingDataLoader(connection.Object,logger.Object);
            subject.LoadData();

            connection.Verify(c => c.Open());
            connection.Verify(c => c.CreateCommand());
            
        }
    }
}