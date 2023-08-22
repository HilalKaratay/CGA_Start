package cga.exercise.game

class Playtime (val morgen: Float, val abend: Float, val playTimeinSek: Int){
    companion object{
        var stopTime = false
        var playtime = 1f
    }

    private var lastPrint = 0f
    var playtime = 0f
    var spielZeit: Int = 0

    fun update(time: Float) {
        if (!stopTime) {
            playtime = (time / playTimeinSek) % 24
        }

        if ((time / playTimeinSek) > spielZeit * 24 + playtime) {
            spielZeit++
            println("ingameTime: 0,00 Uhr")
            lastPrint = 0f
        }

        if (playtime > (lastPrint + 0.5f)) {
            println("ingameTime: %.2f Uhr".format(playtime))
            lastPrint += 0.5f
        }
    }


}