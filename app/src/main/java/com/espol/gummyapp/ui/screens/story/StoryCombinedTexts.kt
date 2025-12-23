package com.espol.gummyapp.ui.screens.story

data class StoryCombinedStep(
    val colorToSend: String, val text: String
)

val storyCombinedSteps = listOf(
    StoryStep(
        "MORADO",
        "Un día, Gommy quería pensar y soñar, pero le faltaba algo para poder empezar: " + "una pieza con ojos y boca, ¡su cabecita para mirar!\n\n" + "¿Dónde está la cabecita?"
    ), StoryStep(
        "ROSA",
        "Quería soñar con salir a pasear, ir por el jardín y también caminar, " + "pero le faltaba su patita rosa en forma de triángulo para avanzar.\n\n" + "¿Dónde está la patita rosa?"
    ), StoryStep(
        "CELESTE",
        "Saltaba en una patita sin poder equilibrar y deseaba caminar sin tropezar; " + "buscaba su patita celeste con forma de óvalo para continuar.\n\n" + "¿Dónde está la patita celeste?"
    ), StoryStep(
        "VERDE",
        "Ya podía caminar, pero quería correr y sentir el viento sin retroceder; " + "así que buscó su patita verde con un corazón para poner.\n\n" + "¿Dónde está la patita verde?"
    ), StoryStep(
        "NARANJA",
        "Casi, casi podía correr sin detenerse, pero aún faltaba una patita para sostenerse: " + "la patita naranja con un pentágono debía ponerse.\n\n" + "¿Dónde está la patita naranja?"
    ), StoryStep(
        "AMARILLO",
        "Gommy estaba tan feliz que quería contarlo, reír, brincar y celebrarlo, " + "pero sin su colita no podía moverla ni lograrlo.\n\n" + "¿Dónde está la colita amarilla?"
    )
)
