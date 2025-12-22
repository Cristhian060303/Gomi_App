package com.espol.gummyapp.ui.screens.story

data class StoryStep(
    val colorToSend: String,
    val text: String
)

val storySteps = listOf(
    StoryStep(
        "MORADO",
        "Gommy caminó un poquito y encontró algo morado,\n" +
                "brillante, bonito… ¡y quedó encantado!\n" +
                "Ayúdalo a encontrar la parte morada de Gommy.\n" +
                "¿Qué parte de Gommy es morada?"
    ),
    StoryStep(
        "ROSA",
        "Luego pensó en algo rosa, suave como una flor,\n" +
                "con un color tierno como el amor.\n" +
                "Ayúdalo a encontrar la pieza rosa.\n" +
                "¿Qué parte de Gommy es rosa?"
    ),
    StoryStep(
        "CELESTE",
        "Alzó su mirada y vio el cielo celeste,\n" +
                "tan claro y brillante hizo que se acueste.\n" +
                "¿Qué parte de Gommy es celeste?"
    ),
    StoryStep(
        "VERDE",
        "En ese bonito día vio el césped crecer,\n" +
                "tan verde y vivo que lo quiso tener.\n" +
                "Ayúdalo a buscar algo verde,\n" +
                "¿Qué parte de Gommy es verde?"
    ),
    StoryStep(
        "NARANJA",
        "Más adelante encontró un árbol lleno de naranjas,\n" +
                "brillantes, jugosas… ¡las guardó en unas cajas!\n" +
                "¿Qué parte de Gommy es naranja?"
    ),
    StoryStep(
        "AMARILLO",
        "Gommy recordó que el sol amarillo ilumina el camino,\n" +
                "y quiso buscar algo igual, un color tan bonito.\n" +
                "¿Qué parte de Gommy es amarilla?"
    )
)