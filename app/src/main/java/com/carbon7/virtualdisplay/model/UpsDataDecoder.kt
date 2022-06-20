package com.carbon7.virtualdisplay.model

class UpsDataDecoder {


    companion object{
        //STATICI PER RIDURRE CREAZIONE DI LISTE PER PIU' VOLTE INUTILMENTE
        private val status  = listOf(
            Status("S000"),
            Status("S001"),
            Status("S002"),
            Status("S003"),
            Status("S004"),
            Status("S005"),
            Status("S006"),
            Status("S007"),
            Status("S008"),
            Status("S009"),
            Status("S010"),
            Status("S011"),
            Status("S012"),
            Status("S013"),
            Status("S014"),
            Status("S015"),
            Status("S016"),
            Status("S017"),
            Status("S018"),
            Status("S019"),
            Status("S020"),
            Status("S021"),
            Status("S022"),
            Status("S023"),
            Status("S024"),
            Status("S025"),
            Status("S026"),
            Status("S027"),
            Status("S028"),
            Status("S029"),
            Status("S030"),
            Status("S031"),
            Status("S032"),
            Status("S033"),
            Status("S034"),
            Status("S035"),
            Status("S036"),
            Status("S037"),
            Status("S038"),
            Status("S039"),
            Status("S040"),
            Status("S041"),
            Status("S042"),
            Status("S043"),
            Status("S044"),
            Status("S045"),
            Status("S046"),
            Status("S047"),
            Status("S048"),
            Status("S049"),
            Status("S050"),
            Status("S051"),
            Status("S052"),
            Status("S053"),
            Status("S054"),
            Status("S055"),
            Status("S056"),
            Status("S057"),
            Status("S058"),
            Status("S059"),
            Status("S060"),
            Status("S061"),
            Status("S062"),
            Status("S063"),
            Status("S064"),
            Status("S065"),
            Status("S066"),
            Status("S067"),
            Status("S068"),
            Status("S069"),
            Status("S070"),
            Status("S071"),
            Status("S072"),
            Status("S073"),
            Status("S074"),
            Status("S075"),
            Status("S076"),
            Status("S077"),
            Status("S078"),
            Status("S079"),
            Status("S080"),
            Status("S081"),
            Status("S082"),
            Status("S083"),
            Status("S084"),
            Status("S085"),
            Status("S086"),
            Status("S087"),
            Status("S088"),
            Status("S089"),
            Status("S090"),
            Status("S091"),
            Status("S092"),
            Status("S093"),
            Status("S094"),
            Status("S095"),
            Status("S096"),
            Status("S097"),
            Status("S098"),
            Status("S099"),
            Status("S100"),
            Status("S101"),
            Status("S102"),
            Status("S103"),
            Status("S104"),
            Status("S105"),
            Status("S106"),
            Status("S107"),
            Status("S108"),
            Status("S109"),
            Status("S110"),
            Status("S111"),
            Status("S112"),
            Status("S113"),
            Status("S114"),
            Status("S115"),
            Status("S116"),
            Status("S117"),
            Status("S118"),
            Status("S119"),
            Status("S120"),
            Status("S121"),
            Status("S122"),
            Status("S123"),
            Status("S124"),
            Status("S125"),
            Status("S126"),
            Status("S127")
        )
        private val alarms = listOf(
            Alarm("A000", Alarm.Level.CRITICAL),
            Alarm("A001", Alarm.Level.CRITICAL),
            Alarm("A002", Alarm.Level.WARNING),
            Alarm("A003", Alarm.Level.WARNING),
            Alarm("A004", Alarm.Level.WARNING),
            Alarm("A005", Alarm.Level.WARNING),
            Alarm("A006", Alarm.Level.WARNING),
            Alarm("A007", Alarm.Level.NONE),
            Alarm("A008", Alarm.Level.WARNING),
            Alarm("A009", Alarm.Level.WARNING),
            Alarm("A010", Alarm.Level.WARNING),
            Alarm("A011", Alarm.Level.NONE),
            Alarm("A012", Alarm.Level.CRITICAL),
            Alarm("A013", Alarm.Level.WARNING),
            Alarm("A014", Alarm.Level.WARNING),
            Alarm("A015", Alarm.Level.WARNING),
            Alarm("A016", Alarm.Level.CRITICAL),
            Alarm("A017", Alarm.Level.CRITICAL),
            Alarm("A018", Alarm.Level.CRITICAL),
            Alarm("A019", Alarm.Level.WARNING),
            Alarm("A020", Alarm.Level.CRITICAL),
            Alarm("A021", Alarm.Level.CRITICAL),
            Alarm("A022", Alarm.Level.WARNING),
            Alarm("A023", Alarm.Level.WARNING),
            Alarm("A024", Alarm.Level.WARNING),
            Alarm("A025", Alarm.Level.WARNING),
            Alarm("A026", Alarm.Level.WARNING),
            Alarm("A027", Alarm.Level.CRITICAL),
            Alarm("A028", Alarm.Level.WARNING),
            Alarm("A029", Alarm.Level.CRITICAL),
            Alarm("A030", Alarm.Level.WARNING),
            Alarm("A031", Alarm.Level.WARNING),
            Alarm("A032", Alarm.Level.CRITICAL),
            Alarm("A033", Alarm.Level.WARNING),
            Alarm("A034", Alarm.Level.WARNING),
            Alarm("A035", Alarm.Level.WARNING),
            Alarm("A036", Alarm.Level.WARNING),
            Alarm("A037", Alarm.Level.CRITICAL),
            Alarm("A038", Alarm.Level.WARNING),
            Alarm("A039", Alarm.Level.NONE),
            Alarm("A040", Alarm.Level.CRITICAL),
            Alarm("A041", Alarm.Level.WARNING),
            Alarm("A042", Alarm.Level.WARNING),
            Alarm("A043", Alarm.Level.NONE),
            Alarm("A044", Alarm.Level.NONE),
            Alarm("A045", Alarm.Level.WARNING),
            Alarm("A046", Alarm.Level.CRITICAL),
            Alarm("A047", Alarm.Level.WARNING),
            Alarm("A048", Alarm.Level.CRITICAL),
            Alarm("A049", Alarm.Level.WARNING),
            Alarm("A050", Alarm.Level.WARNING),
            Alarm("A051", Alarm.Level.WARNING),
            Alarm("A052", Alarm.Level.WARNING),
            Alarm("A053", Alarm.Level.WARNING),
            Alarm("A054", Alarm.Level.WARNING),
            Alarm("A055", Alarm.Level.WARNING),
            Alarm("A056", Alarm.Level.WARNING),
            Alarm("A057", Alarm.Level.WARNING),
            Alarm("A058", Alarm.Level.WARNING),
            Alarm("A059", Alarm.Level.CRITICAL),
            Alarm("A060", Alarm.Level.WARNING),
            Alarm("A061", Alarm.Level.WARNING),
            Alarm("A062", Alarm.Level.WARNING),
            Alarm("A063", Alarm.Level.NONE),
            Alarm("A064", Alarm.Level.NONE),
            Alarm("A065", Alarm.Level.NONE),
            Alarm("A066", Alarm.Level.NONE),
            Alarm("A067", Alarm.Level.NONE),
            Alarm("A068", Alarm.Level.NONE),
            Alarm("A069", Alarm.Level.NONE),
            Alarm("A070", Alarm.Level.NONE),
            Alarm("A071", Alarm.Level.NONE),
            Alarm("A072", Alarm.Level.WARNING),
            Alarm("A073", Alarm.Level.NONE),
            Alarm("A074", Alarm.Level.NONE),
            Alarm("A075", Alarm.Level.NONE),
            Alarm("A076", Alarm.Level.NONE),
            Alarm("A077", Alarm.Level.NONE),
            Alarm("A078", Alarm.Level.NONE),
            Alarm("A079", Alarm.Level.NONE),
            Alarm("A080", Alarm.Level.CRITICAL),
            Alarm("A081", Alarm.Level.NONE),
            Alarm("A082", Alarm.Level.NONE),
            Alarm("A083", Alarm.Level.NONE),
            Alarm("A084", Alarm.Level.NONE),
            Alarm("A085", Alarm.Level.NONE),
            Alarm("A086", Alarm.Level.NONE),
            Alarm("A087", Alarm.Level.NONE),
            Alarm("A088", Alarm.Level.NONE),
            Alarm("A089", Alarm.Level.NONE),
            Alarm("A090", Alarm.Level.NONE),
            Alarm("A091", Alarm.Level.NONE),
            Alarm("A092", Alarm.Level.NONE),
            Alarm("A093", Alarm.Level.NONE),
            Alarm("A094", Alarm.Level.NONE),
            Alarm("A095", Alarm.Level.NONE),
            Alarm("A096", Alarm.Level.NONE),
            Alarm("A097", Alarm.Level.NONE),
            Alarm("A098", Alarm.Level.NONE),
            Alarm("A099", Alarm.Level.NONE),
            Alarm("A100", Alarm.Level.NONE),
            Alarm("A101", Alarm.Level.NONE),
            Alarm("A102", Alarm.Level.NONE),
            Alarm("A103", Alarm.Level.NONE),
            Alarm("A104", Alarm.Level.NONE),
            Alarm("A105", Alarm.Level.NONE),
            Alarm("A106", Alarm.Level.NONE),
            Alarm("A107", Alarm.Level.NONE),
            Alarm("A108", Alarm.Level.NONE),
            Alarm("A109", Alarm.Level.NONE),
            Alarm("A110", Alarm.Level.NONE),
            Alarm("A111", Alarm.Level.NONE),
            Alarm("A112", Alarm.Level.NONE),
            Alarm("A113", Alarm.Level.NONE),
            Alarm("A114", Alarm.Level.NONE),
            Alarm("A115", Alarm.Level.NONE),
            Alarm("A116", Alarm.Level.NONE),
            Alarm("A117", Alarm.Level.NONE),
            Alarm("A118", Alarm.Level.NONE),
            Alarm("A119", Alarm.Level.NONE),
            Alarm("A120", Alarm.Level.NONE),
            Alarm("A121", Alarm.Level.NONE),
            Alarm("A122", Alarm.Level.NONE),
            Alarm("A123", Alarm.Level.NONE),
            Alarm("A124", Alarm.Level.NONE),
            Alarm("A125", Alarm.Level.NONE),
            Alarm("A126", Alarm.Level.NONE),
            Alarm("A127", Alarm.Level.NONE)
        )
        private val measurements = listOf(
            Measurement("M000"),
            Measurement("M001"),
            Measurement("M002"),
            Measurement("M003"),
            Measurement("M004"),
            Measurement("M005"),
            Measurement("M006"),
            Measurement("M007"),
            Measurement("M008"),
            Measurement("M009"),
            Measurement("M010"),
            Measurement("M011"),
            Measurement("M012"),
            Measurement("M013"),
            Measurement("M014"),
            Measurement("M015"),
            Measurement("M016"),
            Measurement("M017"),
            Measurement("M018"),
            Measurement("M019"),
            Measurement("M020"),
            Measurement("M021"),
            Measurement("M022"),
            Measurement("M023"),
            Measurement("M024"),
            Measurement("M025"),
            Measurement("M026"),
            Measurement("M027"),
            Measurement("M028"),
            Measurement("M029"),
            Measurement("M030"),
            Measurement("M031"),
            Measurement("M032"),
            Measurement("M033"),
            Measurement("M034"),
            Measurement("M035"),
            Measurement("M036"),
            Measurement("M037"),
            Measurement("M038"),
            Measurement("M039"),
            Measurement("M040"),
            Measurement("M041"),
            Measurement("M042"),
            Measurement("M043"),
            Measurement("M044"),
            Measurement("M045"),
            Measurement("M046"),
            Measurement("M047"),
            Measurement("M048"),
            Measurement("M049"),
            Measurement("M050"),
            Measurement("M051"),
            Measurement("M052"),
            Measurement("M053"),
            Measurement("M054"),
            Measurement("M055"),
            Measurement("M056"),
            Measurement("M057"),
            Measurement("M058"),
            Measurement("M059"),
            Measurement("M060"),
            Measurement("M061"),
            Measurement("M062"),
            Measurement("M063"),
            Measurement("M064"),
            Measurement("M065"),
            Measurement("M066"),
            Measurement("M067"),
            Measurement("M068"),
            Measurement("M069"),
            Measurement("M070"),
            Measurement("M071"),
            Measurement("M072"),
            Measurement("M073"),
            Measurement("M074"),
            Measurement("M075"),
            Measurement("M076")
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
    //Da implementare
    private val reg0x00E=1

    fun decode(packet :ByteArray): Triple<List<Status>,List<Alarm>,List<Measurement>> {
        decodeStatus(packet.copyOfRange(0, 16))
        decodeAlarms(packet.copyOfRange(16, 32))
        decodeMeasurements(packet.copyOfRange(32,192))
        return Triple(status, alarms, measurements)
    }

    private fun decodeStatus(statusBytes: ByteArray){
        val statusActive = statusBytes.toBooleanArray()
        for (i in status.indices) {//0..127
            status[i].isActive = statusActive[i]
        }
    }
    private fun decodeAlarms(alarmsBytes: ByteArray){
        val alarmsActive = alarmsBytes.toBooleanArray()
        for (i in alarms.indices) {//0..127
            alarms[i].isActive=alarmsActive[i]
        }
    }
    private fun decodeMeasurements(measurementsBytes: ByteArray){
        val measurementsRaw=measurementsBytes.take(77*2).toByteArray().toShorts()//Da M000 a M076

        for (i in measurementsRaw.indices){
            var measurement= measurementsRaw[i]
            if(measurement_factor[i][2]==true)
                measurement= (measurementsRaw[i]-32768).toShort()
            measurements[i].value = measurement.toFloat()/(measurement_factor[i][reg0x00E] as Int)
        }

    }

    //CONVERSIONI UTILI PER LA DECODIFICA
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