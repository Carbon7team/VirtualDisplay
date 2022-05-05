package com.carbon7.virtualdisplay.model

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import com.carbon7.virtualdisplay.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpsDataService: Service() {


    private val binder = LocalBinder()
    private lateinit var ups: Ups
    val eventBus = EventBus<Triple<List<Status>,List<Alarm>,List<Measurement>>>()


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.extras != null) {
            val bundle = intent.extras!!
            val ip=bundle.getString("ip")!!
            val port=bundle.getInt("port")

            ups=ProxyUps(ip,port)
        }
        start()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder(){
        fun getService(): UpsDataService = this@UpsDataService
    }



    companion object{
        private val startingStatus  = listOf(
            Status("S000", R.string.s000),
            Status("S001", R.string.s001),
            Status("S002", R.string.s002),
            Status("S003", R.string.s003),
            Status("S004", R.string.s004),
            Status("S005", R.string.s005),
            Status("S006", R.string.s006),
            Status("S007", R.string.s007),
            Status("S008", R.string.s008),
            Status("S009", R.string.s009),
            Status("S010", R.string.s010),
            Status("S011", R.string.s011),
            Status("S012", R.string.s012),
            Status("S013", R.string.s013),
            Status("S014", R.string.s014),
            Status("S015", R.string.s015),
            Status("S016", R.string.s016),
            Status("S017", R.string.s017),
            Status("S018", R.string.s018),
            Status("S019", R.string.s019),
            Status("S020", R.string.s020),
            Status("S021", R.string.s021),
            Status("S022", R.string.s022),
            Status("S023", R.string.s023),
            Status("S024", R.string.s024),
            Status("S025", R.string.s025),
            Status("S026", R.string.s026),
            Status("S027", R.string.s027),
            Status("S028", R.string.s028),
            Status("S029", R.string.s029),
            Status("S030", R.string.s030),
            Status("S031", R.string.s031),
            Status("S032", R.string.s032),
            Status("S033", R.string.s033),
            Status("S034", R.string.s034),
            Status("S035", R.string.s035),
            Status("S036", R.string.s036),
            Status("S037", R.string.s037),
            Status("S038", R.string.s038),
            Status("S039", R.string.s039),
            Status("S040", R.string.s040),
            Status("S041", R.string.s041),
            Status("S042", R.string.s042),
            Status("S043", R.string.s043),
            Status("S044", R.string.s044),
            Status("S045", R.string.s045),
            Status("S046", R.string.s046),
            Status("S047", R.string.s047),
            Status("S048", R.string.s048),
            Status("S049", R.string.s049),
            Status("S050", R.string.s050),
            Status("S051", R.string.s051),
            Status("S052", R.string.s052),
            Status("S053", R.string.s053),
            Status("S054", R.string.s054),
            Status("S055", R.string.s055),
            Status("S056", R.string.s056),
            Status("S057", R.string.s057),
            Status("S058", R.string.s058),
            Status("S059", R.string.s059),
            Status("S060", R.string.s060),
            Status("S061", R.string.s061),
            Status("S062", R.string.s062),
            Status("S063", R.string.s063),
            Status("S064", R.string.s064),
            Status("S065", R.string.s065),
            Status("S066", R.string.s066),
            Status("S067", R.string.s067),
            Status("S068", R.string.s068),
            Status("S069", R.string.s069),
            Status("S070", R.string.s070),
            Status("S071", R.string.s071),
            Status("S072", R.string.s072),
            Status("S073", R.string.s073),
            Status("S074", R.string.s074),
            Status("S075", R.string.s075),
            Status("S076", R.string.s076),
            Status("S077", R.string.s077),
            Status("S078", R.string.s078),
            Status("S079", R.string.s079),
            Status("S080", R.string.s080),
            Status("S081", R.string.s081),
            Status("S082", R.string.s082),
            Status("S083", R.string.s083),
            Status("S084", R.string.s084),
            Status("S085", R.string.s085),
            Status("S086", R.string.s086),
            Status("S087", R.string.s087),
            Status("S088", R.string.s088),
            Status("S089", R.string.s089),
            Status("S090", R.string.s090),
            Status("S091", R.string.s091),
            Status("S092", R.string.s092),
            Status("S093", R.string.s093),
            Status("S094", R.string.s094),
            Status("S095", R.string.s095),
            Status("S096", R.string.s096),
            Status("S097", R.string.s097),
            Status("S098", R.string.s098),
            Status("S099", R.string.s099),
            Status("S100", R.string.s100),
            Status("S101", R.string.s101),
            Status("S102", R.string.s102),
            Status("S103", R.string.s103),
            Status("S104", R.string.s104),
            Status("S105", R.string.s105),
            Status("S106", R.string.s106),
            Status("S107", R.string.s107),
            Status("S108", R.string.s108),
            Status("S109", R.string.s109),
            Status("S110", R.string.s110),
            Status("S111", R.string.s111),
            Status("S112", R.string.s112),
            Status("S113", R.string.s113),
            Status("S114", R.string.s114),
            Status("S115", R.string.s115),
            Status("S116", R.string.s116),
            Status("S117", R.string.s117),
            Status("S118", R.string.s118),
            Status("S119", R.string.s119),
            Status("S120", R.string.s120),
            Status("S121", R.string.s121),
            Status("S122", R.string.s122),
            Status("S123", R.string.s123),
            Status("S124", R.string.s124),
            Status("S125", R.string.s125),
            Status("S126", R.string.s126),
            Status("S127", R.string.s127)
        )
        private val startingAlarms = listOf(
            Alarm("A000", R.string.a000,Alarm.Level.CRITICAL),
            Alarm("A001", R.string.a001,Alarm.Level.CRITICAL),
            Alarm("A002", R.string.a002,Alarm.Level.WARNING),
            Alarm("A003", R.string.a003,Alarm.Level.WARNING),
            Alarm("A004", R.string.a004,Alarm.Level.WARNING),
            Alarm("A005", R.string.a005,Alarm.Level.WARNING),
            Alarm("A006", R.string.a006,Alarm.Level.WARNING),
            Alarm("A007", R.string.a007,Alarm.Level.NONE),
            Alarm("A008", R.string.a008,Alarm.Level.WARNING),
            Alarm("A009", R.string.a009,Alarm.Level.WARNING),
            Alarm("A010", R.string.a010,Alarm.Level.WARNING),
            Alarm("A011", R.string.a011,Alarm.Level.NONE),
            Alarm("A012", R.string.a012,Alarm.Level.CRITICAL),
            Alarm("A013", R.string.a013,Alarm.Level.WARNING),
            Alarm("A014", R.string.a014,Alarm.Level.WARNING),
            Alarm("A015", R.string.a015,Alarm.Level.WARNING),
            Alarm("A016", R.string.a016,Alarm.Level.CRITICAL),
            Alarm("A017", R.string.a017,Alarm.Level.CRITICAL),
            Alarm("A018", R.string.a018,Alarm.Level.CRITICAL),
            Alarm("A019", R.string.a019,Alarm.Level.WARNING),
            Alarm("A020", R.string.a020,Alarm.Level.CRITICAL),
            Alarm("A021", R.string.a021,Alarm.Level.CRITICAL),
            Alarm("A022", R.string.a022,Alarm.Level.WARNING),
            Alarm("A023", R.string.a023,Alarm.Level.WARNING),
            Alarm("A024", R.string.a024,Alarm.Level.WARNING),
            Alarm("A025", R.string.a025,Alarm.Level.WARNING),
            Alarm("A026", R.string.a026,Alarm.Level.WARNING),
            Alarm("A027", R.string.a027,Alarm.Level.CRITICAL),
            Alarm("A028", R.string.a028,Alarm.Level.WARNING),
            Alarm("A029", R.string.a029,Alarm.Level.CRITICAL),
            Alarm("A030", R.string.a030,Alarm.Level.WARNING),
            Alarm("A031", R.string.a031,Alarm.Level.WARNING),
            Alarm("A032", R.string.a032,Alarm.Level.CRITICAL),
            Alarm("A033", R.string.a033,Alarm.Level.WARNING),
            Alarm("A034", R.string.a034,Alarm.Level.WARNING),
            Alarm("A035", R.string.a035,Alarm.Level.WARNING),
            Alarm("A036", R.string.a036,Alarm.Level.WARNING),
            Alarm("A037", R.string.a037,Alarm.Level.CRITICAL),
            Alarm("A038", R.string.a038,Alarm.Level.WARNING),
            Alarm("A039", R.string.a039,Alarm.Level.NONE),
            Alarm("A040", R.string.a040,Alarm.Level.CRITICAL),
            Alarm("A041", R.string.a041,Alarm.Level.WARNING),
            Alarm("A042", R.string.a042,Alarm.Level.WARNING),
            Alarm("A043", R.string.a043,Alarm.Level.NONE),
            Alarm("A044", R.string.a044,Alarm.Level.NONE),
            Alarm("A045", R.string.a045,Alarm.Level.WARNING),
            Alarm("A046", R.string.a046,Alarm.Level.CRITICAL),
            Alarm("A047", R.string.a047,Alarm.Level.WARNING),
            Alarm("A048", R.string.a048,Alarm.Level.CRITICAL),
            Alarm("A049", R.string.a049,Alarm.Level.WARNING),
            Alarm("A050", R.string.a050,Alarm.Level.WARNING),
            Alarm("A051", R.string.a051,Alarm.Level.WARNING),
            Alarm("A052", R.string.a052,Alarm.Level.WARNING),
            Alarm("A053", R.string.a053,Alarm.Level.WARNING),
            Alarm("A054", R.string.a054,Alarm.Level.WARNING),
            Alarm("A055", R.string.a055,Alarm.Level.WARNING),
            Alarm("A056", R.string.a056,Alarm.Level.WARNING),
            Alarm("A057", R.string.a057,Alarm.Level.WARNING),
            Alarm("A058", R.string.a058,Alarm.Level.WARNING),
            Alarm("A059", R.string.a059,Alarm.Level.CRITICAL),
            Alarm("A060", R.string.a060,Alarm.Level.WARNING),
            Alarm("A061", R.string.a061,Alarm.Level.WARNING),
            Alarm("A062", R.string.a062,Alarm.Level.WARNING),
            Alarm("A063", R.string.a063,Alarm.Level.NONE),
            Alarm("A064", R.string.a064,Alarm.Level.NONE),
            Alarm("A065", R.string.a065,Alarm.Level.NONE),
            Alarm("A066", R.string.a066,Alarm.Level.NONE),
            Alarm("A067", R.string.a067,Alarm.Level.NONE),
            Alarm("A068", R.string.a068,Alarm.Level.NONE),
            Alarm("A069", R.string.a069,Alarm.Level.NONE),
            Alarm("A070", R.string.a070,Alarm.Level.NONE),
            Alarm("A071", R.string.a071,Alarm.Level.NONE),
            Alarm("A072", R.string.a072,Alarm.Level.WARNING),
            Alarm("A073", R.string.a073,Alarm.Level.NONE),
            Alarm("A074", R.string.a074,Alarm.Level.NONE),
            Alarm("A075", R.string.a075,Alarm.Level.NONE),
            Alarm("A076", R.string.a076,Alarm.Level.NONE),
            Alarm("A077", R.string.a077,Alarm.Level.NONE),
            Alarm("A078", R.string.a078,Alarm.Level.NONE),
            Alarm("A079", R.string.a079,Alarm.Level.NONE),
            Alarm("A080", R.string.a080,Alarm.Level.CRITICAL),
            Alarm("A081", R.string.a081,Alarm.Level.NONE),
            Alarm("A082", R.string.a082,Alarm.Level.NONE),
            Alarm("A083", R.string.a083,Alarm.Level.NONE),
            Alarm("A084", R.string.a084,Alarm.Level.NONE),
            Alarm("A085", R.string.a085,Alarm.Level.NONE),
            Alarm("A086", R.string.a086,Alarm.Level.NONE),
            Alarm("A087", R.string.a087,Alarm.Level.NONE),
            Alarm("A088", R.string.a088,Alarm.Level.NONE),
            Alarm("A089", R.string.a089,Alarm.Level.NONE),
            Alarm("A090", R.string.a090,Alarm.Level.NONE),
            Alarm("A091", R.string.a091,Alarm.Level.NONE),
            Alarm("A092", R.string.a092,Alarm.Level.NONE),
            Alarm("A093", R.string.a093,Alarm.Level.NONE),
            Alarm("A094", R.string.a094,Alarm.Level.NONE),
            Alarm("A095", R.string.a095,Alarm.Level.NONE),
            Alarm("A096", R.string.a096,Alarm.Level.NONE),
            Alarm("A097", R.string.a097,Alarm.Level.NONE),
            Alarm("A098", R.string.a098,Alarm.Level.NONE),
            Alarm("A099", R.string.a099,Alarm.Level.NONE),
            Alarm("A100", R.string.a100,Alarm.Level.NONE),
            Alarm("A101", R.string.a101,Alarm.Level.NONE),
            Alarm("A102", R.string.a102,Alarm.Level.NONE),
            Alarm("A103", R.string.a103,Alarm.Level.NONE),
            Alarm("A104", R.string.a104,Alarm.Level.NONE),
            Alarm("A105", R.string.a105,Alarm.Level.NONE),
            Alarm("A106", R.string.a106,Alarm.Level.NONE),
            Alarm("A107", R.string.a107,Alarm.Level.NONE),
            Alarm("A108", R.string.a108,Alarm.Level.NONE),
            Alarm("A109", R.string.a109,Alarm.Level.NONE),
            Alarm("A110", R.string.a110,Alarm.Level.NONE),
            Alarm("A111", R.string.a111,Alarm.Level.NONE),
            Alarm("A112", R.string.a112,Alarm.Level.NONE),
            Alarm("A113", R.string.a113,Alarm.Level.NONE),
            Alarm("A114", R.string.a114,Alarm.Level.NONE),
            Alarm("A115", R.string.a115,Alarm.Level.NONE),
            Alarm("A116", R.string.a116,Alarm.Level.NONE),
            Alarm("A117", R.string.a117,Alarm.Level.NONE),
            Alarm("A118", R.string.a118,Alarm.Level.NONE),
            Alarm("A119", R.string.a119,Alarm.Level.NONE),
            Alarm("A120", R.string.a120,Alarm.Level.NONE),
            Alarm("A121", R.string.a121,Alarm.Level.NONE),
            Alarm("A122", R.string.a122,Alarm.Level.NONE),
            Alarm("A123", R.string.a123,Alarm.Level.NONE),
            Alarm("A124", R.string.a124,Alarm.Level.NONE),
            Alarm("A125", R.string.a125,Alarm.Level.NONE),
            Alarm("A126", R.string.a126,Alarm.Level.NONE),
            Alarm("A127", R.string.a127,Alarm.Level.NONE)
        )
        private val startingMeasurement = listOf(
            Measurement("M000", null),
            Measurement("M001", null),
            Measurement("M002", null),
            Measurement("M003", null),
            Measurement("M004", null),
            Measurement("M005", null),
            Measurement("M006", null),
            Measurement("M007", null),
            Measurement("M008", null),
            Measurement("M009", null),
            Measurement("M010", null),
            Measurement("M011", null),
            Measurement("M012", null),
            Measurement("M013", null),
            Measurement("M014", null),
            Measurement("M015", null),
            Measurement("M016", null),
            Measurement("M017", null),
            Measurement("M018", null),
            Measurement("M019", null),
            Measurement("M020", null),
            Measurement("M021", null),
            Measurement("M022", null),
            Measurement("M023", null),
            Measurement("M024", null),
            Measurement("M025", null),
            Measurement("M026", null),
            Measurement("M027", null),
            Measurement("M028", null),
            Measurement("M029", null),
            Measurement("M030", null),
            Measurement("M031", null),
            Measurement("M032", null),
            Measurement("M033", null),
            Measurement("M034", null),
            Measurement("M035", null),
            Measurement("M036", null),
            Measurement("M037", null),
            Measurement("M038", null),
            Measurement("M039", null),
            Measurement("M040", null),
            Measurement("M041", null),
            Measurement("M042", null),
            Measurement("M043", null),
            Measurement("M044", null),
            Measurement("M045", null),
            Measurement("M046", null),
            Measurement("M047", null),
            Measurement("M048", null),
            Measurement("M049", null),
            Measurement("M050", null),
            Measurement("M051", null),
            Measurement("M052", null),
            Measurement("M053", null),
            Measurement("M054", null),
            Measurement("M055", null),
            Measurement("M056", null),
            Measurement("M057", null),
            Measurement("M058", null),
            Measurement("M059", null),
            Measurement("M060", null),
            Measurement("M061", null),
            Measurement("M062", null),
            Measurement("M063", null),
            Measurement("M064", null),
            Measurement("M065", null),
            Measurement("M066", null),
            Measurement("M067", null),
            Measurement("M068", null),
            Measurement("M069", null),
            Measurement("M070", null),
            Measurement("M071", null),
            Measurement("M072", null),
            Measurement("M073", null),
            Measurement("M074", null),
            Measurement("M075", null),
            Measurement("M076", null)
        )

        private val measurement_factor= arrayOf(
            arrayOf(1, 1, false),     //MO00
            arrayOf(1, 1, false),     //MO01
            arrayOf(1, 1, false),     //MO02
            arrayOf(1, 1, false),     //MO03
            arrayOf(1, 10, false),    //MO04
            arrayOf(1, 10, false),    //MO05
            arrayOf(1, 10, false),    //MO06
            arrayOf(1, 10, false),    //MO07
            arrayOf(1, 10, false),    //MO08
            arrayOf(1, 10, false),    //MO09
            arrayOf(1, 1, false),     //MO10
            arrayOf(1, 1, false),     //MO11
            arrayOf(1, 1, false),     //MO12
            arrayOf(10, 10, false),   //MO13
            arrayOf(10, 10, false),   //MO14
            arrayOf(10, 10, false),   //MO15
            arrayOf(1, 10, false),    //MO16
            arrayOf(1, 10, false),    //MO17
            arrayOf(1, 10, false),    //MO18
            arrayOf(1, 10, false),    //MO19
            arrayOf(1, 1, false),     //MO20 -> NULL
            arrayOf(1, 1, false),     //MO21
            arrayOf(1, 1, false),     //MO22
            arrayOf(1, 10, false),    //MO23
            arrayOf(1, 1, false),     //MO24
            arrayOf(1, 1, false),     //MO25
            arrayOf(10, 10, false),   //MO26
            arrayOf(10, 10, false),   //MO27
            arrayOf(1, 10, false),    //MO28
            arrayOf(10, 10, false),   //MO29
            arrayOf(1, 1, false),     //MO30
            arrayOf(1, 1, false),     //MO31 -> NULL
            arrayOf(1, 1, false),     //MO32
            arrayOf(1, 1, false),     //MO33
            arrayOf(1, 1, false),     //MO34
            arrayOf(10, 10, false),   //MO35
            arrayOf(1, 1, false),     //MO36
            arrayOf(1, 1, false),     //MO37
            arrayOf(1, 1, false),     //MO38
            arrayOf(1, 1, false),     //MO39
            arrayOf(1, 1, false),     //MO40
            arrayOf(1, 1, false),     //MO41
            arrayOf(10, 10, false),   //MO42
            arrayOf(1, 1, false),     //MO43
            arrayOf(1, 1, false),     //M044
            arrayOf(1, 1, false),     //MO45
            arrayOf(1, 1, false),     //M046
            arrayOf(1, 1, false),     //MO47 -> RESERVED
            arrayOf(1, 10, false),    //MO48
            arrayOf(1, 10, false),    //MO49
            arrayOf(1, 10, false),    //MO50
            arrayOf(1, 10, false),    //MO51
            arrayOf(1, 10, false),    //MO52
            arrayOf(1, 10, false),    //MO53
            arrayOf(1, 1, false),     //MO54
            arrayOf(1, 1, false),     //MO55
            arrayOf(1, 1, false),     //MO56
            arrayOf(100, 100, false), //MO57
            arrayOf(100, 100, false), //MO58
            arrayOf(100, 100, false), //MO59
            arrayOf(10, 10, false),   //MO60
            arrayOf(10, 10, false),   //MO61
            arrayOf(10, 10, false),   //MO62
            arrayOf(10, 10, false),   //MO63
            arrayOf(1, 10, false),    //MO64
            arrayOf(1, 10, false),    //MO65
            arrayOf(1, 10, false),    //MO66
            arrayOf(1, 10, true),     //MO67
            arrayOf(1, 10, true),     //MO68
            arrayOf(1, 10, true),     //MO69
            arrayOf(1, 10, false),     //MO70
            arrayOf(1, 10, false),     //MO71
            arrayOf(1, 10, false),     //MO72
            arrayOf(1, 10, false),     //MO73
            arrayOf(1, 10, false),     //MO74
            arrayOf(1, 10, false),     //MO75
            arrayOf(1, 1, false),     //MO76
        )//M77, M78 and M79 have custom behaviour
    }


    private val reg0x00E=1

    private var status: List<Status> = startingStatus
        private set
    private var alarms: List<Alarm> = startingAlarms
        private set
    private var measurements: List<Measurement> = startingMeasurement
        private set

    private val timer= object: CountDownTimer(Long.MAX_VALUE,2000){
        override fun onTick(p0: Long) {
            CoroutineScope(Dispatchers.Default).launch{
                try {
                    val packet = ups.requestInfo()
                    decode(packet)
                    eventBus.invokeEvent(Triple(status,alarms,measurements))
                }catch (e: Exception){
                    Log.d("MyApp",e.toString())
                }
            }
        }
        override fun onFinish() {}
    }


    fun start(){
        ups.open()
        timer.start()
    }
    fun stop() {
        timer.cancel()
        ups.close()
    }


    private fun decode(packet :ByteArray) {
        decodeStatus(packet.copyOfRange(0, 16))
        decodeAlarms(packet.copyOfRange(16, 32))
        decodeMeasurements(packet.copyOfRange(32,192))
    }

    private fun decodeStatus(statusBytes: ByteArray){
        val statusActive = statusBytes.toBooleanArray()
        for (i in 0 until status.size) {//0..127
            status[i].isActive = statusActive[i]
        }
    }
    private fun decodeAlarms(alarmsBytes: ByteArray){
        val alarmsActive = alarmsBytes.toBooleanArray()
        for (i in 0 until alarms.size) {//0..127
            alarms[i].isActive=alarmsActive[i]
        }
    }
    private fun decodeMeasurements(measurementsBytes: ByteArray){
        val measurements_raw=measurementsBytes.take(77*2).toByteArray().toShorts()//Da M000 a M076

        for (i in 0 until 76){
            var measurement= measurements_raw[i]
            if(measurement_factor[i][2]==true)
                measurement= (measurements_raw[i]-32768).toShort()
            measurement
            measurements[i].value = measurement.toFloat()/(measurement_factor[i][reg0x00E] as Int)
        }

    }


    private fun ByteArray.toBooleanArray(): BooleanArray{
        return toBinaryString().map{it=='1'}.toBooleanArray()
    }
    private fun ByteArray.toBinaryString(separator:String=""):String{
        return this.joinToString(separator) { "%02x".format(it).toInt(16).toBinary(8) }
    }
    private fun Int.toBinary(len: Int): String {
        return String.format("%" + len + "s", this.toString(2)).replace(" ".toRegex(), "0")
    }
    private fun ByteArray.toShorts(): List<Short> {
        return this.toList().chunked(2).map{(it[0].toShort()*256+it[1].toShort()).toShort()}
    }

}