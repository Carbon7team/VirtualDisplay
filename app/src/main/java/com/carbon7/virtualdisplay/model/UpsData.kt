package com.carbon7.virtualdisplay.model

import com.carbon7.virtualdisplay.R
import kotlin.concurrent.thread


class UpsData(u: Ups): Observer, Subject() {

    companion object{
        val allStatus : Map<String, Status> = mapOf(
            "S000" to Status("S000", R.string.s000,false),
            "S001" to Status("S001", R.string.s001,false),
            "S002" to Status("S002", R.string.s002,false),
            "S003" to Status("S003", R.string.s003,false),
            "S004" to Status("S004", R.string.s004,false),
            "S005" to Status("S005", R.string.s005,false),
            "S006" to Status("S006", R.string.s006,false),
            "S007" to Status("S007", R.string.s007,false),
            "S008" to Status("S008", R.string.s008,false),
            "S009" to Status("S009", R.string.s009,false),
            "S010" to Status("S010", R.string.s010,false),
            "S011" to Status("S011", R.string.s011,false),
            "S012" to Status("S012", R.string.s012,false),
            "S013" to Status("S013", R.string.s013,false),
            "S014" to Status("S014", R.string.s014,false),
            "S015" to Status("S015", R.string.s015,false),
            "S016" to Status("S016", R.string.s016,false),
            "S017" to Status("S017", R.string.s017,false),
            "S018" to Status("S018", R.string.s018,false),
            "S019" to Status("S019", R.string.s019,false),
            "S020" to Status("S020", R.string.s020,false),
            "S021" to Status("S021", R.string.s021,false),
            "S022" to Status("S022", R.string.s022,false),
            "S023" to Status("S023", R.string.s023,false),
            "S024" to Status("S024", R.string.s024,false),
            "S025" to Status("S025", R.string.s025,false),
            "S026" to Status("S026", R.string.s026,false),
            "S027" to Status("S027", R.string.s027,false),
            "S028" to Status("S028", R.string.s028,false),
            "S029" to Status("S029", R.string.s029,false),
            "S030" to Status("S030", R.string.s030,false),
            "S031" to Status("S031", R.string.s031,false),
            "S032" to Status("S032", R.string.s032,false),
            "S033" to Status("S033", R.string.s033,false),
            "S034" to Status("S034", R.string.s034,false),
            "S035" to Status("S035", R.string.s035,false),
            "S036" to Status("S036", R.string.s036,false),
            "S037" to Status("S037", R.string.s037,false),
            "S038" to Status("S038", R.string.s038,false),
            "S039" to Status("S039", R.string.s039,false),
            "S040" to Status("S040", R.string.s040,false),
            "S041" to Status("S041", R.string.s041,false),
            "S042" to Status("S042", R.string.s042,false),
            "S043" to Status("S043", R.string.s043,false),
            "S044" to Status("S044", R.string.s044,false),
            "S045" to Status("S045", R.string.s045,false),
            "S046" to Status("S046", R.string.s046,false),
            "S047" to Status("S047", R.string.s047,false),
            "S048" to Status("S048", R.string.s048,false),
            "S049" to Status("S049", R.string.s049,false),
            "S050" to Status("S050", R.string.s050,false),
            "S051" to Status("S051", R.string.s051,false),
            "S052" to Status("S052", R.string.s052,false),
            "S053" to Status("S053", R.string.s053,false),
            "S054" to Status("S054", R.string.s054,false),
            "S055" to Status("S055", R.string.s055,false),
            "S056" to Status("S056", R.string.s056,false),
            "S057" to Status("S057", R.string.s057,false),
            "S058" to Status("S058", R.string.s058,false),
            "S059" to Status("S059", R.string.s059,false),
            "S060" to Status("S060", R.string.s060,false),
            "S061" to Status("S061", R.string.s061,false),
            "S062" to Status("S062", R.string.s062,false),
            "S063" to Status("S063", R.string.s063,false),
            "S064" to Status("S064", R.string.s064,false),
            "S065" to Status("S065", R.string.s065,false),
            "S066" to Status("S066", R.string.s066,false),
            "S067" to Status("S067", R.string.s067,false),
            "S068" to Status("S068", R.string.s068,false),
            "S069" to Status("S069", R.string.s069,false),
            "S070" to Status("S070", R.string.s070,false),
            "S071" to Status("S071", R.string.s071,false),
            "S072" to Status("S072", R.string.s072,false),
            "S073" to Status("S073", R.string.s073,false),
            "S074" to Status("S074", R.string.s074,false),
            "S075" to Status("S075", R.string.s075,false),
            "S076" to Status("S076", R.string.s076,false),
            "S077" to Status("S077", R.string.s077,false),
            "S078" to Status("S078", R.string.s078,false),
            "S079" to Status("S079", R.string.s079,false),
            "S080" to Status("S080", R.string.s080,false),
            "S081" to Status("S081", R.string.s081,false),
            "S082" to Status("S082", R.string.s082,false),
            "S083" to Status("S083", R.string.s083,false),
            "S084" to Status("S084", R.string.s084,false),
            "S085" to Status("S085", R.string.s085,false),
            "S086" to Status("S086", R.string.s086,false),
            "S087" to Status("S087", R.string.s087,false),
            "S088" to Status("S088", R.string.s088,false),
            "S089" to Status("S089", R.string.s089,false),
            "S090" to Status("S090", R.string.s090,false),
            "S091" to Status("S091", R.string.s091,false),
            "S092" to Status("S092", R.string.s092,false),
            "S093" to Status("S093", R.string.s093,false),
            "S094" to Status("S094", R.string.s094,false),
            "S095" to Status("S095", R.string.s095,false),
            "S096" to Status("S096", R.string.s096,false),
            "S097" to Status("S097", R.string.s097,false),
            "S098" to Status("S098", R.string.s098,false),
            "S099" to Status("S099", R.string.s099,false),
            "S100" to Status("S100", R.string.s100,false),
            "S101" to Status("S101", R.string.s101,false),
            "S102" to Status("S102", R.string.s102,false),
            "S103" to Status("S103", R.string.s103,false),
            "S104" to Status("S104", R.string.s104,false),
            "S105" to Status("S105", R.string.s105,false),
            "S106" to Status("S106", R.string.s106,false),
            "S107" to Status("S107", R.string.s107,false),
            "S108" to Status("S108", R.string.s108,false),
            "S109" to Status("S109", R.string.s109,false),
            "S110" to Status("S110", R.string.s110,false),
            "S111" to Status("S111", R.string.s111,false),
            "S112" to Status("S112", R.string.s112,false),
            "S113" to Status("S113", R.string.s113,false),
            "S114" to Status("S114", R.string.s114,false),
            "S115" to Status("S115", R.string.s115,false),
            "S116" to Status("S116", R.string.s116,false),
            "S117" to Status("S117", R.string.s117,false),
            "S118" to Status("S118", R.string.s118,false),
            "S119" to Status("S119", R.string.s119,false),
            "S120" to Status("S120", R.string.s120,false),
            "S121" to Status("S121", R.string.s121,false),
            "S122" to Status("S122", R.string.s122,false),
            "S123" to Status("S123", R.string.s123,false),
            "S124" to Status("S124", R.string.s124,false),
            "S125" to Status("S125", R.string.s125,false),
            "S126" to Status("S126", R.string.s126,false),
            "S127" to Status("S127", R.string.s127,false)
        )
        val allAlarms: Map<String, Alarm> = mapOf(
            "A000" to Alarm("A000", R.string.a000,Alarm.Level.NONE),
            "A001" to Alarm("A001", R.string.a001,Alarm.Level.NONE),
            "A002" to Alarm("A002", R.string.a002,Alarm.Level.NONE),
            "A003" to Alarm("A003", R.string.a003,Alarm.Level.NONE),
            "A004" to Alarm("A004", R.string.a004,Alarm.Level.NONE),
            "A005" to Alarm("A005", R.string.a005,Alarm.Level.NONE),
            "A006" to Alarm("A006", R.string.a006,Alarm.Level.NONE),
            "A007" to Alarm("A007", R.string.a007,Alarm.Level.NONE),
            "A008" to Alarm("A008", R.string.a008,Alarm.Level.NONE),
            "A009" to Alarm("A009", R.string.a009,Alarm.Level.NONE),
            "A010" to Alarm("A010", R.string.a010,Alarm.Level.NONE),
            "A011" to Alarm("A011", R.string.a011,Alarm.Level.NONE),
            "A012" to Alarm("A012", R.string.a012,Alarm.Level.NONE),
            "A013" to Alarm("A013", R.string.a013,Alarm.Level.NONE),
            "A014" to Alarm("A014", R.string.a014,Alarm.Level.NONE),
            "A015" to Alarm("A015", R.string.a015,Alarm.Level.NONE),
            "A016" to Alarm("A016", R.string.a016,Alarm.Level.NONE),
            "A017" to Alarm("A017", R.string.a017,Alarm.Level.NONE),
            "A018" to Alarm("A018", R.string.a018,Alarm.Level.NONE),
            "A019" to Alarm("A019", R.string.a019,Alarm.Level.NONE),
            "A020" to Alarm("A020", R.string.a020,Alarm.Level.NONE),
            "A021" to Alarm("A021", R.string.a021,Alarm.Level.NONE),
            "A022" to Alarm("A022", R.string.a022,Alarm.Level.NONE),
            "A023" to Alarm("A023", R.string.a023,Alarm.Level.NONE),
            "A024" to Alarm("A024", R.string.a024,Alarm.Level.NONE),
            "A025" to Alarm("A025", R.string.a025,Alarm.Level.NONE),
            "A026" to Alarm("A026", R.string.a026,Alarm.Level.NONE),
            "A027" to Alarm("A027", R.string.a027,Alarm.Level.NONE),
            "A028" to Alarm("A028", R.string.a028,Alarm.Level.NONE),
            "A029" to Alarm("A029", R.string.a029,Alarm.Level.NONE),
            "A030" to Alarm("A030", R.string.a030,Alarm.Level.NONE),
            "A031" to Alarm("A031", R.string.a031,Alarm.Level.NONE),
            "A032" to Alarm("A032", R.string.a032,Alarm.Level.NONE),
            "A033" to Alarm("A033", R.string.a033,Alarm.Level.NONE),
            "A034" to Alarm("A034", R.string.a034,Alarm.Level.NONE),
            "A035" to Alarm("A035", R.string.a035,Alarm.Level.NONE),
            "A036" to Alarm("A036", R.string.a036,Alarm.Level.NONE),
            "A037" to Alarm("A037", R.string.a037,Alarm.Level.NONE),
            "A038" to Alarm("A038", R.string.a038,Alarm.Level.NONE),
            "A039" to Alarm("A039", R.string.a039,Alarm.Level.NONE),
            "A040" to Alarm("A040", R.string.a040,Alarm.Level.NONE),
            "A041" to Alarm("A041", R.string.a041,Alarm.Level.NONE),
            "A042" to Alarm("A042", R.string.a042,Alarm.Level.NONE),
            "A043" to Alarm("A043", R.string.a043,Alarm.Level.NONE),
            "A044" to Alarm("A044", R.string.a044,Alarm.Level.NONE),
            "A045" to Alarm("A045", R.string.a045,Alarm.Level.NONE),
            "A046" to Alarm("A046", R.string.a046,Alarm.Level.NONE),
            "A047" to Alarm("A047", R.string.a047,Alarm.Level.NONE),
            "A048" to Alarm("A048", R.string.a048,Alarm.Level.NONE),
            "A049" to Alarm("A049", R.string.a049,Alarm.Level.NONE),
            "A050" to Alarm("A050", R.string.a050,Alarm.Level.NONE),
            "A051" to Alarm("A051", R.string.a051,Alarm.Level.NONE),
            "A052" to Alarm("A052", R.string.a052,Alarm.Level.NONE),
            "A053" to Alarm("A053", R.string.a053,Alarm.Level.NONE),
            "A054" to Alarm("A054", R.string.a054,Alarm.Level.NONE),
            "A055" to Alarm("A055", R.string.a055,Alarm.Level.NONE),
            "A056" to Alarm("A056", R.string.a056,Alarm.Level.NONE),
            "A057" to Alarm("A057", R.string.a057,Alarm.Level.NONE),
            "A058" to Alarm("A058", R.string.a058,Alarm.Level.NONE),
            "A059" to Alarm("A059", R.string.a059,Alarm.Level.NONE),
            "A060" to Alarm("A060", R.string.a060,Alarm.Level.NONE),
            "A061" to Alarm("A061", R.string.a061,Alarm.Level.NONE),
            "A062" to Alarm("A062", R.string.a062,Alarm.Level.NONE),
            "A063" to Alarm("A063", R.string.a063,Alarm.Level.NONE),
            "A064" to Alarm("A064", R.string.a064,Alarm.Level.NONE),
            "A065" to Alarm("A065", R.string.a065,Alarm.Level.NONE),
            "A066" to Alarm("A066", R.string.a066,Alarm.Level.NONE),
            "A067" to Alarm("A067", R.string.a067,Alarm.Level.NONE),
            "A068" to Alarm("A068", R.string.a068,Alarm.Level.NONE),
            "A069" to Alarm("A069", R.string.a069,Alarm.Level.NONE),
            "A070" to Alarm("A070", R.string.a070,Alarm.Level.NONE),
            "A071" to Alarm("A071", R.string.a071,Alarm.Level.NONE),
            "A072" to Alarm("A072", R.string.a072,Alarm.Level.NONE),
            "A073" to Alarm("A073", R.string.a073,Alarm.Level.NONE),
            "A074" to Alarm("A074", R.string.a074,Alarm.Level.NONE),
            "A075" to Alarm("A075", R.string.a075,Alarm.Level.NONE),
            "A076" to Alarm("A076", R.string.a076,Alarm.Level.NONE),
            "A077" to Alarm("A077", R.string.a077,Alarm.Level.NONE),
            "A078" to Alarm("A078", R.string.a078,Alarm.Level.NONE),
            "A079" to Alarm("A079", R.string.a079,Alarm.Level.NONE),
            "A080" to Alarm("A080", R.string.a080,Alarm.Level.NONE),
            "A081" to Alarm("A081", R.string.a081,Alarm.Level.NONE),
            "A082" to Alarm("A082", R.string.a082,Alarm.Level.NONE),
            "A083" to Alarm("A083", R.string.a083,Alarm.Level.NONE),
            "A084" to Alarm("A084", R.string.a084,Alarm.Level.NONE),
            "A085" to Alarm("A085", R.string.a085,Alarm.Level.NONE),
            "A086" to Alarm("A086", R.string.a086,Alarm.Level.NONE),
            "A087" to Alarm("A087", R.string.a087,Alarm.Level.NONE),
            "A088" to Alarm("A088", R.string.a088,Alarm.Level.NONE),
            "A089" to Alarm("A089", R.string.a089,Alarm.Level.NONE),
            "A090" to Alarm("A090", R.string.a090,Alarm.Level.NONE),
            "A091" to Alarm("A091", R.string.a091,Alarm.Level.NONE),
            "A092" to Alarm("A092", R.string.a092,Alarm.Level.NONE),
            "A093" to Alarm("A093", R.string.a093,Alarm.Level.NONE),
            "A094" to Alarm("A094", R.string.a094,Alarm.Level.NONE),
            "A095" to Alarm("A095", R.string.a095,Alarm.Level.NONE),
            "A096" to Alarm("A096", R.string.a096,Alarm.Level.NONE),
            "A097" to Alarm("A097", R.string.a097,Alarm.Level.NONE),
            "A098" to Alarm("A098", R.string.a098,Alarm.Level.NONE),
            "A099" to Alarm("A099", R.string.a099,Alarm.Level.NONE),
            "A100" to Alarm("A100", R.string.a100,Alarm.Level.NONE),
            "A101" to Alarm("A101", R.string.a101,Alarm.Level.NONE),
            "A102" to Alarm("A102", R.string.a102,Alarm.Level.NONE),
            "A103" to Alarm("A103", R.string.a103,Alarm.Level.NONE),
            "A104" to Alarm("A104", R.string.a104,Alarm.Level.NONE),
            "A105" to Alarm("A105", R.string.a105,Alarm.Level.NONE),
            "A106" to Alarm("A106", R.string.a106,Alarm.Level.NONE),
            "A107" to Alarm("A107", R.string.a107,Alarm.Level.NONE),
            "A108" to Alarm("A108", R.string.a108,Alarm.Level.NONE),
            "A109" to Alarm("A109", R.string.a109,Alarm.Level.NONE),
            "A110" to Alarm("A110", R.string.a110,Alarm.Level.NONE),
            "A111" to Alarm("A111", R.string.a111,Alarm.Level.NONE),
            "A112" to Alarm("A112", R.string.a112,Alarm.Level.NONE),
            "A113" to Alarm("A113", R.string.a113,Alarm.Level.NONE),
            "A114" to Alarm("A114", R.string.a114,Alarm.Level.NONE),
            "A115" to Alarm("A115", R.string.a115,Alarm.Level.NONE),
            "A116" to Alarm("A116", R.string.a116,Alarm.Level.NONE),
            "A117" to Alarm("A117", R.string.a117,Alarm.Level.NONE),
            "A118" to Alarm("A118", R.string.a118,Alarm.Level.NONE),
            "A119" to Alarm("A119", R.string.a119,Alarm.Level.NONE),
            "A120" to Alarm("A120", R.string.a120,Alarm.Level.NONE),
            "A121" to Alarm("A121", R.string.a121,Alarm.Level.NONE),
            "A122" to Alarm("A122", R.string.a122,Alarm.Level.NONE),
            "A123" to Alarm("A123", R.string.a123,Alarm.Level.NONE),
            "A124" to Alarm("A124", R.string.a124,Alarm.Level.NONE),
            "A125" to Alarm("A125", R.string.a125,Alarm.Level.NONE),
            "A126" to Alarm("A126", R.string.a126,Alarm.Level.NONE),
            "A127" to Alarm("A127", R.string.a127,Alarm.Level.NONE)
        )
    }

    var ups: Ups = u
    /*set(value) {
        field?.close()
        field = value
    }*/

    private var running = false
    var status: Map<String, Status>? = null
        private set
    var alarms: Map<String, Alarm>? = null
        private set
    var measurements: Map<String, Measurement>? = null
        private set

    init {
        ups.addObserver(this)
    }

    fun start(){
        if(!running){
            running=true
            thread {
                while(running){
                    ups.requestInfo()
                    Thread.sleep(5000)
                }
            }
        }
    }
    fun stop() {
        running=false
        ups.removeObserver(this)
    }


    override fun update() {
        val upsPacket = ups.getState()
        if (upsPacket != null) {
            status = decodeStatus(upsPacket.copyOfRange(0, 16))
            alarms = decodeAlarms(upsPacket.copyOfRange(16, 32))
            notify()
        }
    }

    private fun decodeStatus(statusBytes: ByteArray):Map<String, Status>{
        val newStatus = allStatus.toMutableMap()

        val statusActive = statusBytes.toBooleanArray()
        for (i in 0 until allStatus.size) {//0..127
            newStatus["S%03d".format(i)]?.isActive = statusActive[i]
        }

        return newStatus
    }
    private fun decodeAlarms(alarmsBytes: ByteArray): Map<String, Alarm>{
        val newAlarm = mutableMapOf<String, Alarm>()

        val alarmActive = alarmsBytes.toBooleanArray()
        for (i in 0 until allAlarms.size) {//0..127
            if (alarmActive[i])
                newAlarm["A%03d".format(i)] = allAlarms["A%03d".format(i)]!!
        }

        return newAlarm
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




}