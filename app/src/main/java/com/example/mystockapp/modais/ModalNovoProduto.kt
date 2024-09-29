import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.R
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.lojaApi.LojaService
import com.example.mystockapp.api.produtoApi.CorService
import com.example.mystockapp.api.produtoApi.ModeloService
import com.example.mystockapp.api.produtoApi.ProdutoService
import com.example.mystockapp.api.produtoApi.TamanhoService
import com.example.mystockapp.modais.ConfirmacaoDialog

// componentes
import com.example.mystockapp.modais.ModalHeaderComponent
import com.example.mystockapp.modais.SucessoDialog
import com.example.mystockapp.modais.componentes.ButtonComponent
import com.example.mystockapp.modais.componentes.SelectField
import com.example.mystockapp.models.lojas.Loja
import com.example.mystockapp.models.produtos.Cor
import com.example.mystockapp.models.produtos.ItemPromocional
import com.example.mystockapp.models.produtos.Modelo
import com.example.mystockapp.models.produtos.ProdutoCreate
import com.example.mystockapp.models.produtos.Tamanho
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun NovoProdutoDialog(onDismissRequest: () -> Unit, context: Context = androidx.compose.ui.platform.LocalContext.current) {
    var isPromocional by remember { mutableStateOf(false) }
    var codigo by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf(Modelo(-1, "", "", "")) }
    var tamanho by remember { mutableStateOf(Tamanho(-1, -1)) }
    var nome by remember { mutableStateOf("") }
    var precoCusto by remember { mutableStateOf("") }
    var precoVenda by remember { mutableStateOf("") }
    var cor by remember { mutableStateOf(Cor(-1, "")) }
    var nItens by remember { mutableStateOf("") }

    var modelosOptions by remember { mutableStateOf<List<Modelo>>(emptyList())}
    var coresOptions by remember { mutableStateOf<List<Cor>>(emptyList())}
    var tamanhosOptions by remember { mutableStateOf<List<Tamanho>>(emptyList()) }

    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    var showError by remember { mutableStateOf(false) }
    var showSucessoDialog by remember { mutableStateOf(false) }
    var confirmarTitulo by remember { mutableStateOf("") }
    var actionToPerform by remember { mutableStateOf<suspend () -> Unit>({}) }
    var confirmarBtnTitulo by remember { mutableStateOf("") }
    var recusarBtnTitulo by remember { mutableStateOf("") }
    var bgCorBtn by remember { mutableStateOf(Color(0xFF355070)) }
    var imgCasoDeErro by remember { mutableStateOf<Int?>(null) }

    var loja by remember { mutableStateOf(Loja())}
    val gson = Gson()
    val sharedPreferences = context.getSharedPreferences("MyStockPrefs", Context.MODE_PRIVATE)
    val idLoja = sharedPreferences.getInt("idLoja", -1) // -1 é o valor padrão caso não encontre

    fun handleAbrirModalConfirm(
        titulo: String,
        action: suspend () -> Unit,
        confirmarTexto: String,
        recusarTexto: String,
        corBtn: Color
    ) {
        confirmarTitulo = titulo
        actionToPerform = action
        confirmarBtnTitulo = confirmarTexto
        recusarBtnTitulo = recusarTexto
        bgCorBtn = corBtn
        showSucessoDialog = true
    }

    LaunchedEffect(Unit) {
        val modeloSerivce = ModeloService(RetrofitInstance.modeloApi)
        val tamanhoService = TamanhoService(RetrofitInstance.tamanhoApi)
        val corService = CorService(RetrofitInstance.corApi)
        val lojaService = LojaService(RetrofitInstance.lojaApi)
        try {
            modelosOptions = modeloSerivce.fetchModelos()
            tamanhosOptions = tamanhoService.fetchTamanhos()
            coresOptions = corService.fetchCores()
            loja = lojaService.fetchLojaById(idLoja);
        } catch (e: ApiException) {
            errorMessage = "${e.message}"
        } catch (e: NetworkException) {
            errorMessage = "Network Error: ${e.message}"
        } catch (e: GeneralException) {
            errorMessage = "${e.message}"
        }
    }

    suspend fun handleSaveProduto(
        codigo: String,
        cor: Cor,
        modelo: Modelo,
        tamanho: Tamanho,
        nome: String,
        loja: Loja,
        valorCusto: Double,
        valorRevenda: Double,
        itemPromocional: Boolean,
        quantidade: Int
    ) {
        val produtoService = ProdutoService(RetrofitInstance.produtoApi)
        try {
            val objeto = ProdutoCreate(
                codigo = codigo,
                idCor = cor.id,
                idModelo = modelo.id,
                nome = nome,
                valorCusto = valorCusto,
                valorRevenda = valorRevenda,
                tamanho = tamanho.numero,
                itemPromocional = if (itemPromocional) ItemPromocional.SIM.name else ItemPromocional.NAO.name,
                idLoja = loja.id,
                quantidade = quantidade
            )
            Log.e("NovoProdutoDialog", "OBJETO DE INPUT: ${gson.toJson(objeto)}")
            val response = produtoService.createProduto(objeto
                )
            when(response.code()) {
                201 -> {
                    Log.d("NovoProdutoDialog", "Produto salvo com sucesso")
                    confirmarTitulo = "Produto salvo com sucesso"
                }
                400 -> {
                    Log.e("NovoProdutoDialog", "OBJETO DE INPUT: ${gson.toJson(objeto)}")
                    Log.e("NovoProdutoDialog", "Erro ao salvar produto - dados incorretos: ${response.errorBody()}")
                    errorMessage = "Dados não preenchidos corretamente"
                    confirmarTitulo = "Dados não preenchidos corretamente"
                    imgCasoDeErro = R.mipmap.ic_excluir
                }
                500 -> {
                    Log.e("NovoProdutoDialog", "Erro inesperado, tente novamente: ${response.errorBody()}")
                    errorMessage = "Erro inesperado, tente novamente"
                    confirmarTitulo = "Erro inesperado, tente novamente ou entre em contato com o suporte"
                    imgCasoDeErro = R.mipmap.ic_excluir
                }
                409 ->{
                    Log.e("NovoProdutoDialog", "Erro ao salvar produto: ${response.errorBody()}")
                    errorMessage = "Produto já cadastrado"
                    confirmarTitulo = "Produto já cadastrado"
                    imgCasoDeErro = R.mipmap.ic_excluir
                }
                else -> {
                    Log.e("NovoProdutoDialog", "Erro ao salvar produto: ${response.errorBody()}")
                }
            }

            handleAbrirModalConfirm(
                confirmarTitulo,
                action = { showSucessoDialog = true },
                confirmarTexto = "Ok",
                recusarTexto = "",
                corBtn = Color(0xFF355070)
            )
        } catch (e: ApiException) {
            Log.e("NovoProdutoDialog", "ApiException: ${e.message}")
            handleAbrirModalConfirm(
                confirmarTitulo,
                action = { showSucessoDialog = true },
                confirmarTexto = "Ok",
                recusarTexto = "",
                corBtn = Color(0xFF355070)
            )
        } catch (e: NetworkException) {
            Log.e("NovoProdutoDialog", "NetworkException: ${e.message}")
        } catch (e: GeneralException) {
            Log.e("NovoProdutoDialog", "GeneralException: ${e.message}")
        }
    }


    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(15.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ModalHeaderComponent(onDismissRequest = onDismissRequest, "Novo Produto")

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE7E7E7))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                com.example.mystockapp.modais.FormField(
                                    label = "Codigo:",
                                    textValue = codigo,
                                    fieldType = KeyboardType.Number,
                                    onValueChange = { codigo = it },
                                    error = showError && codigo.isEmpty()
                                )
                                SelectField(
                                    label = "Modelo:",
                                    selectedOption = modelo.nome,
                                    options = modelosOptions.map {it.nome},
                                    onOptionSelected = {
                                        modeloNome ->
                                        val selectedModelo = modelosOptions.find { it.nome == modeloNome }
                                        selectedModelo?.let { modelo = it }
                                    },
                                    error = showError && modelo.id == -1
                                )
                                SelectField(
                                    label = "Tamanho:",
                                    selectedOption = tamanho.numero.toString(),
                                    options = tamanhosOptions.map { it.numero.toString() },
                                    onOptionSelected = { tamanhoNome ->
                                        val selectedTamanho = tamanhosOptions.find { it.numero.toString() == tamanhoNome }
                                        selectedTamanho?.let { tamanho = it }
                                    },
                                    error = showError && tamanho.id == -1
                                )
                                SelectField(
                                    label = "Cor:",
                                    selectedOption = cor.nome,
                                    options = coresOptions.map{ it.nome},
                                    onOptionSelected = {
                                            corSelected ->
                                        val selectedCor = coresOptions.find { it.nome == corSelected }
                                        selectedCor?.let { cor = it }
                                    },
                                    error = showError && cor.id == -1
                                )
                                com.example.mystockapp.components.FormFieldCheck(
                                    label = "Item Promocional",
                                    isChecked = isPromocional,
                                    onCheckedChange = { isPromocional = it }
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                com.example.mystockapp.modais.FormField(
                                    label = "Nome:",
                                    textValue = nome,
                                    onValueChange = { nome = it },
                                    error = showError && nome.isEmpty()
                                )
                                com.example.mystockapp.modais.FormField(
                                    label = "Preço Custo:",
                                    textValue = precoCusto,
                                    fieldType = KeyboardType.Decimal,
                                    onValueChange = { precoCusto = it },
                                    error = showError && precoCusto.isEmpty()
                                )
                                com.example.mystockapp.modais.FormField(
                                    label = "Preço Venda:",
                                    textValue = precoVenda,
                                    fieldType = KeyboardType.Decimal,
                                    onValueChange = { precoVenda = it },
                                    error = showError && precoVenda.isEmpty()
                                )
                                com.example.mystockapp.modais.FormField(
                                    label = "N. Itens:",
                                    textValue = nItens,
                                    fieldType = KeyboardType.Number,
                                    onValueChange = { nItens = it },
                                    error = showError && nItens.isEmpty()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ButtonComponent(
                        titulo = "Limpar",
                        onClick = onDismissRequest,
                        containerColor = Color(0xFF919191),
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    ButtonComponent(
                        titulo = "Salvar",
                        onClick = {
                            if (codigo.isNotEmpty() && modelo.id != -1 && tamanho.id != -1 && nome.isNotEmpty() && precoCusto.isNotEmpty() && precoVenda.isNotEmpty() && cor.id != -1 && nItens.isNotEmpty()) {
                                coroutineScope.launch {
                                        handleSaveProduto(
                                            codigo,
                                            cor,
                                            modelo,
                                            tamanho,
                                            nome,
                                            loja,
                                            precoCusto.toDouble(),
                                            precoVenda.toDouble(),
                                            isPromocional,
                                            nItens.toInt()
                                        )
                                }
                            } else {
                                showError = true
                                errorMessage = "Preencha todos os campos"
                                imgCasoDeErro = R.mipmap.ic_excluir
                            }
                        },
                        containerColor = Color(0xFF355070),
                    )
                }
            }
        }
    }
    if (showSucessoDialog) {
        SucessoDialog(
            titulo = confirmarTitulo,
            onDismiss = {
                showSucessoDialog = false
                onDismissRequest()
            },
            onConfirm = {
                showSucessoDialog = false
                onDismissRequest()
            },
            btnConfirmColor = Color(0xFF355070),
            imagem = imgCasoDeErro?.let { painterResource(id = it) } ?: painterResource(id = R.mipmap.ic_sucesso),
            btnConfirmTitulo = "OK"
        )
    }
}
@Preview
@Composable
fun NovoProdutoDialogPreview() {
    // Simulando o comportamento de dismiss para visualização no preview
    NovoProdutoDialog(onDismissRequest = {})
}
