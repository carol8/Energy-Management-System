import com.google.gson.Gson
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.concurrent.timerTask

fun main(args: Array<String>) {
    val timeMultiplier = 6000
    val initialTime = System.currentTimeMillis()

    val fileReader = File(args[0]).bufferedReader()
    val csvParser = CSVParser(fileReader, CSVFormat.DEFAULT);
    val devices = LinkedHashMap<String, Long>();

    val factory = ConnectionFactory()
    val connection = factory.newConnection()

    val dataFile = File(args[1]).bufferedReader()
    val dataParser = CSVParser(dataFile, CSVFormat.DEFAULT)
    val data = ArrayList<String>()

    for (csvRecord in csvParser) {
        devices[csvRecord[0]] = csvRecord[1].toLong()
    }

    for (dataRecord in dataParser) {
        data.add(dataRecord[0]);
    }

    for (device in devices) {
        val channel = connection.createChannel()

        channel.queueDeclare(device.key, true, false, false, null)
        Timer(device.key, true).scheduleAtFixedRate(
            timerTask {
                deviceRunner(
                    channel,
                    DeviceData(
                        initialTime + (System.currentTimeMillis() - initialTime) * timeMultiplier,
                        device.key,
                        data[(0..<200).random()].toDouble()
                    )
                )
            },
            0,
            device.value
        )
    }

    while (true);
}

fun deviceRunner(channel: Channel, deviceData: DeviceData) {

    val gson = Gson()
    val deviceDataJson = gson.toJson(deviceData)
    channel.basicPublish(
        "",
        deviceData.deviceId,
        null,
        deviceDataJson.toByteArray(StandardCharsets.UTF_8)
    )
    println("Sent $deviceDataJson on queue ${deviceData.deviceId} (equivalent timestamp ${LocalDateTime.ofInstant(Instant.ofEpochMilli(deviceData.timestamp), ZoneId.systemDefault())})")
}