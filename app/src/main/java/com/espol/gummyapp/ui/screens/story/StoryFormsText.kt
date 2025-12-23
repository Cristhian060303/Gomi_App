package com.espol.gummyapp.ui.screens.story

data class StoryFormStep(
    val colorToSend: String, val text: String
)

val storyFormsSteps = listOf(
    StoryFormStep(
        "MORADO",
        "Un día, Gommy quería una naranja, redonda y bonita… ¡como la luna llena!\n" + "\"Esa forma es un círculo\", dijo con brillo en los ojos.\n" + "¿Dónde está el círculo?"
    ), StoryFormStep(
        "ROSA",
        "Luego vio una montaña alta y puntiaguda, con forma de triángulo.\n" + "¡Qué forma tan aguda!\n" + "\"Yo quiero un triángulo\", dijo con encanto.\n" + "¿Dónde está el triángulo?"
    ), StoryFormStep(
        "CELESTE",
        "Después vio una sandía larga y genial, con forma de óvalo.\n" + "¡Ayúdalo a encontrarla!\n" + "\"¿Dónde está mi óvalo?\", preguntó contento."
    ), StoryFormStep(
        "VERDE",
        "Gommy estaba feliz y dibujó un corazón,\n" + "una forma bonita y llena de emoción.\n" + "¿Puedes encontrar un corazón?"
    ), StoryFormStep(
        "NARANJA",
        "Regresó a su casa y sonrió mucho.\n" + "Su casa tenía cinco lados… ¡un pentágono!\n" + "\"¿Hay una pieza con forma de pentágono?\", dijo Gommy curioso."
    ), StoryFormStep(
        "AMARILLO",
        "Luego miró su ventana, firme y ordenada,\n" + "con cuatro lados iguales… ¡una forma cuadrada!\n" + "\"¿Dónde está mi cuadrado?\", dijo emocionado."
    )
)
