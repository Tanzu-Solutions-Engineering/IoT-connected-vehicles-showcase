package com.vmware.maintence.prediction.interference.ai

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.tribuo.MutableDataset
import org.tribuo.Trainer
import org.tribuo.classification.Label
import org.tribuo.classification.LabelFactory
import org.tribuo.classification.dtree.CARTClassificationTrainer
import org.tribuo.classification.dtree.impurity.Entropy
import org.tribuo.classification.evaluation.LabelEvaluation
import org.tribuo.classification.evaluation.LabelEvaluator
import org.tribuo.data.csv.CSVLoader
import org.tribuo.dataset.DatasetView
import org.tribuo.json.JsonDataSource
import java.nio.file.Paths


class InferMaintenanceConsumerTest {

    private lateinit var repository : VehicleRepository
    private val vehicle: Vehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create()
    private lateinit var subject: InferMaintenanceConsumer

    @BeforeEach
    fun setUp() {
        repository = mock<VehicleRepository>()
        subject = InferMaintenanceConsumer(repository)
    }

    @Test
    fun apply() {
        var actual = subject.accept(vehicle)
        verify(repository).save(Mockito.any(Vehicle::class.java))

    }

    @Test
    fun train() {

        val labelFactory = LabelFactory()
        val csvLoader = CSVLoader(labelFactory)
        val responseName = "label"
        var trainSource : MutableDataset<Label> = csvLoader.load(Paths.get("src/test/resources/csv/car-maintenance.csv"),responseName )

        val treeTrainer: Trainer<Label> = CARTClassificationTrainer(6, 10f, 0.0f, 1.0f, false, Entropy(), 1L)
//        val dtTrainer: Trainer<MultiLabel> = IndependentMultiLabelTrainer(treeTrainer)

        var dtModel = treeTrainer.train(trainSource)

        val evaluator = LabelEvaluator()
        //int version, String className
        val version = 1
        val className = ""
        val jsonSource: JsonDataSource<*> = JsonDataSource<Any?>(jsonPath, rowProcessor, true)

        val datasetFromJson = MutableDataset<Label>(jsonSource)
        val evaluation: LabelEvaluation = evaluator.evaluate(dtModel, DatasetView.deserializeFromProto(version,className,vehicle))
        println(evaluation.toString())

//        dtModel.

        println("Model: $dtModel")
    }
}