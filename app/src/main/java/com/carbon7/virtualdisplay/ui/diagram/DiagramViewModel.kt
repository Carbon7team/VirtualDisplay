package com.carbon7.virtualdisplay.ui.diagram

import androidx.lifecycle.*
import com.carbon7.virtualdisplay.model.Alarm
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerViewModel

class DiagramViewModel : UpsDataVisualizerViewModel() {
    // TODO: Implement the ViewModel

    enum class State { ON, OFF, PREV, CRITIC, SYMBOL, ENABLE, DISABLE}
    enum class BatteryStatus { CHARGE, DISCHARGE, OK, OPEN}
    enum class OutputState {PREV_BAT, ON_INV, PREV_BYP, ON_ECO, OFF }

    val recInputState = Transformations.map(status){ s->
            when {
                s[39].isActive -> State.PREV
                s[48].isActive -> State.ON
                else -> State.OFF
            }
    }

    val recState = Transformations.switchMap(alarms){ a ->
        Transformations.map(status){ s->
            when {
                a[32].isActive -> State.CRITIC
                a[33].isActive && s[49].isActive -> State.PREV
                else -> State.SYMBOL
            }
        }
    }

    val dcInputState = Transformations.map(status){ s->
        when {
            s[39].isActive -> State.PREV
            s[49].isActive -> State.ON
            else -> State.OFF
        }
    }

    val dcBusState = Transformations.map(status){ s->
        when {
            s[39].isActive -> State.PREV
            s[49].isActive -> State.ON
            else -> State.OFF
        }
    }

    val dcOutputState = Transformations.switchMap(alarms){ a ->
        Transformations.map(status){ s->
            when {
                a[19].isActive && s[39].isActive -> State.PREV
                else -> State.OFF
            }
        }
    }

    val invInputState = Transformations.switchMap(alarms){ a ->
        Transformations.map(status){ s->
            when {
                a[19].isActive && s[39].isActive -> State.PREV
                s[49].isActive -> State.ON
                else -> State.OFF
            }
        }
    }

    val outputState = Transformations.switchMap(alarms){ a ->
        Transformations.map(status){ s->
            when {
                s[0].isActive && a[19].isActive -> OutputState.PREV_BAT
                s[0].isActive -> OutputState.ON_INV
                s[2].isActive && !(s[6].isActive) -> OutputState.PREV_BYP
                s[2].isActive && s[6].isActive -> OutputState.ON_ECO
                else -> OutputState.OFF
            }
        }
    }

    val invState = Transformations.switchMap(alarms){ a ->
        Transformations.map(status){ s->
            when {
                a[40].isActive -> State.CRITIC
                a[41].isActive && s[52].isActive -> State.PREV
                else -> State.SYMBOL
            }
        }
    }

    val invOutputState = Transformations.switchMap(alarms){ a ->
        Transformations.map(status){ s->
            when {
                a[19].isActive -> State.PREV
                s[52].isActive -> State.ON
                else -> State.OFF
            }
        }
    }

    val bypInputState = Transformations.map(status){ s->
        when {
            s[2].isActive && !(s[6].isActive) -> State.PREV
            s[56].isActive -> State.ON
            else -> State.OFF
        }
    }

    val bypState = Transformations.switchMap(alarms){ a ->
        Transformations.map(status){ s->
            when {
                a[48].isActive && s[57].isActive -> State.CRITIC
                a[49].isActive && s[57].isActive -> State.PREV
                else -> State.SYMBOL
            }
        }
    }

    val bypOutputState = Transformations.map(status){ s->
        when {
            s[2].isActive && !(s[6].isActive) -> State.PREV
            s[2].isActive && s[6].isActive -> State.ON
            s[57].isActive && !(s[2].isActive) -> State.ON
            else -> State.OFF
        }
    }

    val mbpPresentState = Transformations.map(status){ s->
        State.OFF
    }

    val obMntBypState = Transformations.map(status){ s->
        when {
            s[3].isActive  -> State.PREV
            else -> null
        }
    }

    val loadStatus = Transformations.map(measurement){ m ->
        when{
            m[0].value!! > 90 && m[0].value!! <= 110 -> State.PREV
            m[0].value!! > 110 -> State.CRITIC
            else -> State.ON
        }
    }

    val loadValue = Transformations.map(measurement){ m ->
        if (m[0].value==null) ""
        else {m[0].value!!.toInt().toString()+" %"}
    }

    val loadValueStatus = Transformations.map(measurement){ m ->
        m[0].value!!.toInt()
    }

    val bypImpossState = Transformations.map(alarms){ a->
        when {
            a[3].isActive  -> State.CRITIC
            a[4].isActive  -> State.PREV
            else -> null
        }
    }

    val gensetOnState = Transformations.map(status){ s->
        when {
            s[23].isActive  -> State.SYMBOL
            else -> null
        }
    }

    val batteryState = Transformations.map(alarms){ a->
        when {
            a[27].isActive -> State.CRITIC
            a[20].isActive || a[21].isActive || a[22].isActive || a[26].isActive -> State.PREV
            else -> State.SYMBOL
        }
    }

    val batteryStatus = Transformations.switchMap(alarms){ a ->
        Transformations.map(status){ s->
            when {
                a[16].isActive -> BatteryStatus.OPEN
                a[19].isActive -> BatteryStatus.DISCHARGE
                s[36].isActive -> BatteryStatus.CHARGE
                s[32].isActive -> BatteryStatus.OK
                else -> BatteryStatus.OK
            }
        }
    }

    val batteryLevelStatus = Transformations.switchMap(batteryStatus){ bs ->
        Transformations.map(measurement){ m->
            var lev =  0.toInt()
            if (bs!=BatteryStatus.DISCHARGE){ lev = m[22].value!!.toInt() }
            else { lev = m[24].value!!.toInt()}

            when {
                lev % 5 <= 3 -> lev + lev % 5
                else -> lev
            }
        }
    }

    val batteryDescription = Transformations.switchMap(batteryStatus){ bs ->
        Transformations.map(measurement){ m->
            if (bs!=BatteryStatus.DISCHARGE) {
                when {
                    m[22].value==null -> ""
                    m[22].value!!.toInt()==0 -> m[22].value!!.toInt().toString()
                    else -> m[22].value!!.toInt().toString()+" %"
                }
            }
            else {
                when {
                    m[24].value==null -> "---"
                    m[24].value!!.toInt() < 2 -> "---"
                    else -> m[24].value!!.toInt().toString()+"'"
                }
            }
        }
    }




}