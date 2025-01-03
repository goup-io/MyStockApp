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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.mystockapp.api.produtoApi.ProdutoService

// componentes
import com.example.mystockapp.modais.FormField
import com.example.mystockapp.components.FormFieldCheck
import com.example.mystockapp.modais.ConfirmacaoDialog
import com.example.mystockapp.modais.ModalHeaderComponent
import com.example.mystockapp.modais.SucessoDialog
import com.example.mystockapp.modais.componentes.ButtonComponent
import com.example.mystockapp.modais.componentes.SelectField
import com.example.mystockapp.modais.componentes.utils.desformatarPreco
import com.example.mystockapp.modais.componentes.utils.formatarPreco
import com.example.mystockapp.models.lojas.Loja
import com.example.mystockapp.models.produtos.ItemPromocional
import com.example.mystockapp.models.produtos.Produto
import com.example.mystockapp.models.produtos.ProdutoEdit
import com.example.mystockapp.models.produtos.ProdutoEditarGet
import com.example.mystockapp.models.produtos.ProdutoTable
import kotlinx.coroutines.launch


@Composable
fun InformacoesProdutoDialog(onDismissRequest: () -> Unit, idProduto: Int) {
    var modelosOptions by remember { mutableStateOf(listOf("")) }
    var coresOptions by remember { mutableStateOf(listOf("")) }
    var tamanhosOptions by remember { mutableStateOf(listOf(0)) }
    var loja by remember { mutableStateOf(Loja()) }
    var produtoInfo by remember { mutableStateOf(ProdutoEditarGet()) }
    var isPromocional by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSucessoDialog by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var confirmarTitulo by remember { mutableStateOf("") }
    var confirmarImagem by remember { mutableStateOf<Int?>(null) }
    var actionToPerform by remember { mutableStateOf<suspend () -> Unit>({}) } // Variável para armazenar a ação
    var confirmarBtnTitulo by remember { mutableStateOf("") }
    var recusarBtnTitulo by remember { mutableStateOf("") }
    var bgCorBtn by remember { mutableStateOf(Color(0xFF355070)) }
    var imgCasoDeErro by remember { mutableStateOf<Int?>(null) }

    var tempValorCusto by remember { mutableStateOf("") }
    var tempValorVenda by remember { mutableStateOf("") }

    // contexto local (a tela atual)
    val contexto = LocalContext.current

    // Função que controla a exibição do modal
    fun handleAbrirModalConfirm(
        titulo: String,
        imagemResId: Int,
        action: suspend () -> Unit,
        confirmarTexto: String,
        recusarTexto: String,
        corBtn: Color
    ) {
        confirmarTitulo = titulo
        confirmarImagem = imagemResId
        showConfirmDialog = true
        actionToPerform = action
        confirmarBtnTitulo = confirmarTexto
        recusarBtnTitulo = recusarTexto
        bgCorBtn = corBtn
    }


    LaunchedEffect(Unit) {
        try {
            val produtoService = ProdutoService(RetrofitInstance.produtoApi)
            val lojaService = LojaService(RetrofitInstance.lojaApi)
            produtoInfo = produtoService.fetchProdutosParaEditarTabela(idProduto)
            isPromocional = produtoInfo.itemPromocional == "SIM"
            tempValorCusto = formatarPreco(produtoInfo.precoCusto.toString().replace(".", ","))
            tempValorVenda = formatarPreco(produtoInfo.precoRevenda.toString().replace(".", ","))
            loja = lojaService.fetchLojaById(produtoInfo.idLoja)
        } catch (e: ApiException) {
            errorMessage = "${e.message}"
            Log.e("InformacoesProdutoDialog", "ApiException: ${e.message}")
        } catch (e: NetworkException) {
            errorMessage = "Network Error: ${e.message}"
            Log.e("InformacoesProdutoDialog", "NetworkException: ${e.message}")
        } catch (e: GeneralException) {
            errorMessage = "${e.message}"
            Log.e("InformacoesProdutoDialog", "GeneralException: ${e.message}")
        } catch (e: Exception) {
            errorMessage = "Unexpected Error: ${e.message}"
            Log.e("InformacoesProdutoDialog", "Exception: ${e.message}")
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
                ModalHeaderComponent(
                    onDismissRequest = onDismissRequest,
                    stringResource(id = R.string.informacoes_produto_titulo)
                )
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
                                FormField(
                                    label = stringResource(id = R.string.codigo_label),
                                    textValue = produtoInfo.codigo,
                                    onValueChange = { produtoInfo = produtoInfo.copy(codigo = it) },
                                    fieldType = KeyboardType.Number,
                                    error = showError && produtoInfo.codigo.isEmpty()
                                )
                                SelectField(
                                    label = stringResource(id = R.string.modelo_label),
                                    selectedOption = produtoInfo.modelo,
                                    options = modelosOptions,
                                    disabled = true,
                                    onOptionSelected = { produtoInfo = produtoInfo.copy(modelo = it) },
                                    error = showError && produtoInfo.modelo.isEmpty()
                                )
                                SelectField(
                                    label = stringResource(id = R.string.tamanho_label),
                                    selectedOption = produtoInfo.tamanho.toString(),
                                    disabled = true,
                                    options = tamanhosOptions,
                                    onOptionSelected = { produtoInfo = produtoInfo.copy(tamanho = it.toInt()) },
                                )
                                SelectField(
                                    label = stringResource(id = R.string.cor_label),
                                    selectedOption = produtoInfo.cor,
                                    disabled = true,
                                    options = coresOptions,
                                    onOptionSelected = { produtoInfo = produtoInfo.copy(cor = it) }
                                )
                                FormFieldCheck(
                                    label = stringResource(id = R.string.item_promocional_label),
                                    isChecked = isPromocional,
                                    onCheckedChange = {
                                        isPromocional = it
                                        if (isPromocional) {
                                            produtoInfo.itemPromocional = "SIM"
                                        } else {
                                            produtoInfo.itemPromocional = "NAO"
                                        }
                                    }
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                FormField(
                                    label = stringResource(id = R.string.nome_label),
                                    textValue = produtoInfo.nome,
                                    onValueChange = { produtoInfo = produtoInfo.copy(nome = it) },
                                    error = showError && produtoInfo.nome.isEmpty()
                                )
                                FormField(
                                    label = stringResource(id = R.string.preco_custo_label),
                                    textValue = tempValorCusto,
                                    fieldType = KeyboardType.Decimal,
                                    onValueChange = { input ->
                                        val precoFormatado = formatarPreco(input)
                                        tempValorCusto = precoFormatado
                                        produtoInfo = produtoInfo.copy(precoCusto = desformatarPreco(precoFormatado))
                                        Log.d("InformacoesProdutoDialog", "Preço Custo: ${produtoInfo.precoCusto}")
                                    },
                                    error = showError && produtoInfo.precoCusto <= 0.0
                                )
                                FormField(
                                    label = stringResource(id = R.string.preco_venda_label),
                                    textValue = tempValorVenda,
                                    fieldType = KeyboardType.Decimal,
                                    onValueChange = { newValue ->
                                        val precoFormatado = formatarPreco(newValue)
                                        tempValorVenda = precoFormatado
                                        produtoInfo = produtoInfo.copy(precoRevenda = desformatarPreco(precoFormatado))
                                        Log.d("InformacoesProdutoDialog", "Preço Venda: ${produtoInfo.precoRevenda}")
                                    },
                                    error = showError && produtoInfo.precoRevenda <= 0.0
                                )
                                FormField(
                                    label = stringResource(id = R.string.quantidade_itens_label),
                                    textValue = produtoInfo.quantidade.toString(),
                                    fieldType = KeyboardType.Number,
                                    onValueChange = { newValue ->
                                        val regex = "^[0-9]*$".toRegex()
                                        if (newValue.matches(regex)) {
                                            produtoInfo = produtoInfo.copy(quantidade = newValue.toIntOrNull() ?: 0)
                                        }
                                    },
                                    error = showError && produtoInfo.quantidade < 0
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
                        titulo = stringResource(id = R.string.excluir_button_text),
                        onClick = {
                            handleAbrirModalConfirm(
                                contexto.getString(R.string.confirm_excluir_titulo),
                                R.mipmap.ic_excluir,
                                { handleExcluirProd(idProduto) },
                                contexto.getString(R.string.excluir_confirm_button),
                                contexto.getString(R.string.cancel_button),
                                Color(0xFFD93D3D)
                            )
                        },
                        containerColor = Color(0xFFD93D3D),
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    ButtonComponent(
                        titulo = stringResource(id = R.string.editar_button_text),
                        onClick = {
                            handleAbrirModalConfirm(
                                contexto.getString(R.string.confirm_editar_titulo),
                                R.mipmap.ic_editar,
                                { handleEditarProd(idProduto, produtoInfo) },
                                contexto.getString(R.string.editar_confirm_button),
                                contexto.getString(R.string.cancel_button),
                                Color(0xFFBEA54C)
                            )
                        },
                        containerColor = Color(0xFF355070),
                    )
                }
            }
        }
    }
    if (showConfirmDialog && confirmarImagem != null) {
        ConfirmacaoDialog(
            titulo = confirmarTitulo,
            confirmarBtnTitulo = confirmarBtnTitulo,
            recusarBtnTitulo = recusarBtnTitulo,
            imagem = painterResource(id = confirmarImagem!!),
            onConfirm = {
                coroutineScope.launch {
                    try {
                        actionToPerform()
                        showConfirmDialog = false
                        showSucessoDialog = true
                    } catch (e: Exception) {
                        Log.e("InformacoesProdutoDialog", "Erro ao executar ação: ${e.message}")
                        imgCasoDeErro = R.mipmap.ic_excluir
                    }
                }
            },
            onDismiss = {
                showConfirmDialog = false
            },
            btnConfirmColor = bgCorBtn
        )
    }

    if (showSucessoDialog) {
        SucessoDialog(
            titulo = contexto.getString(R.string.acao_sucesso_titulo),
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

suspend fun handleEditarProd(idProduto: Int, produtoInfo: ProdutoEditarGet) {
    Log.d("InformacoesProdutoDialog", "Editando o produto com id: $idProduto")
    val produtoEditDto = ProdutoEdit(
        produtoInfo.codigo,
        produtoInfo.nome,
        produtoInfo.precoCusto.toDouble(),
        produtoInfo.precoRevenda.toDouble(),
        produtoInfo.itemPromocional.uppercase(),
        produtoInfo.quantidade
    )
    Log.d("InformacoesProdutoDialog", "Informações a passar: produtoEditDto: $produtoEditDto")

    try {
        val produtoService = ProdutoService(RetrofitInstance.produtoApi)
        var response = produtoService.editarEtp(idProduto, produtoEditDto)
        Log.d("InformacoesProdutoDialog", "Produto editado com sucesso: $response")
    } catch (e: ApiException) {
        Log.e("InformacoesProdutoDialog", "ApiException: ${e.message}")
    } catch (e: NetworkException) {
        Log.e("InformacoesProdutoDialog", "NetworkException: ${e.message}")
    } catch (e: GeneralException) {
        Log.e("InformacoesProdutoDialog", "GeneralException: ${e.message}")
    } catch (e: Exception) {
        Log.e("InformacoesProdutoDialog", "Exception: ${e.message}")
    } catch (e: ApiException) {
        Log.e("InformacoesProdutoDialog", "ApiException: ${e.code} - ${e.message}")
    }
}

suspend fun handleExcluirProd(idProduto: Int){
    Log.d("InformacoesProdutoDialog", "Excluindo o produto com id: $idProduto")
    try {
        val produtoService = ProdutoService(RetrofitInstance.produtoApi)
        var response = produtoService.deleteEtp(idProduto)
        Log.d("InformacoesProdutoDialog", "Produto excluido com sucesso: $response")
    } catch (e: ApiException) {
        Log.e("InformacoesProdutoDialog", "ApiException: ${e.message}")
    } catch (e: NetworkException) {
        Log.e("InformacoesProdutoDialog", "NetworkException: ${e.message}")
    } catch (e: GeneralException) {
        Log.e("InformacoesProdutoDialog", "GeneralException: ${e.message}")
    } catch (e: Exception) {
        Log.e("InformacoesProdutoDialog", "Exception: ${e.message}")
    } catch (e: ApiException) {
        Log.e("InformacoesProdutoDialog", "ApiException: ${e.code} - ${e.message}")
    }
}

@Preview
@Composable
fun InformacoesProdutoDialogPreview() {
    // Simulando o comportamento de dismiss para visualização no preview
    InformacoesProdutoDialog(onDismissRequest = {}, idProduto = 1)
}
