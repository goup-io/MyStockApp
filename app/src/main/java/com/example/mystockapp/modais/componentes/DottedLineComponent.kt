import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DottedLineComponent(
    color: Color = Color(0xFF355070),
    thickness: Float = 5f, // Espessura da linha
    dotLength: Float = 10f, // Comprimento do ponto
    gapLength: Float = 15f // Espaço entre os pontos
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth() // Faz a linha ocupar a largura total
            .height(thickness.dp) // Define a altura da linha de acordo com a espessura
    ) {
        val canvasWidth = size.width
        var startX = 0f

        // Desenha a linha pontilhada
        while (startX < canvasWidth) {
            drawLine(
                color = color,
                start = Offset(startX, 0f),
                end = Offset(startX + dotLength, 0f),
                strokeWidth = thickness,
                cap = StrokeCap.Round // Faz com que cada ponto tenha bordas arredondadas
            )
            startX += dotLength + gapLength // Move para o próximo ponto
        }
    }
}
