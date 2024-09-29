package com.example.mystockapp.modais

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mystockapp.R
import com.example.mystockapp.api.RetrofitInstance
import com.example.mystockapp.api.exceptions.ApiException
import com.example.mystockapp.api.exceptions.GeneralException
import com.example.mystockapp.api.exceptions.NetworkException
import com.example.mystockapp.api.produtoApi.CategoriaService
import com.example.mystockapp.api.produtoApi.ModeloService
import com.example.mystockapp.api.produtoApi.TipoService

// componentes
import com.example.mystockapp.modais.componentes.ButtonComponent
import com.example.mystockapp.modais.componentes.SelectField
import com.example.mystockapp.models.produtos.Categoria
import com.example.mystockapp.models.produtos.ModeloReq
import com.example.mystockapp.models.produtos.Tipo
import kotlinx.coroutines.launch


@Composable
fun ModalNovoModeloDialog(onDismissRequest: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(Tipo(id = 0, nome = "")) }
    var categoria by remember { mutableStateOf(Categoria(id = 0, nome = "")) }

    var tiposOptions by remember { mutableStateOf<List<Tipo>>(emptyList()) }
    var categoriasOptions by remember { mutableStateOf<List<Categoria>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    var showSucessDialog by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var confirmarTitulo by remember { mutableStateOf("") }
    var actionToPerform by remember { mutableStateOf<suspend () -> Unit>({}) }
    var confirmarBtnTitulo by remember { mutableStateOf("") }
    var recusarBtnTitulo by remember { mutableStateOf("") }
    var bgCorBtn by remember { mutableStateOf(Color(0xFF355070)) }
    var imgCasoDeErro by remember { mutableStateOf<Int?>(null) }

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
    }

    LaunchedEffect(Unit) {
        val categoriaService = CategoriaService(RetrofitInstance.categoriaApi)
        val tipoService = TipoService(RetrofitInstance.tipoApi)
        try {
            categoriasOptions = categoriaService.fetchCategorias()
            tiposOptions = tipoService.fetchTipos()
        } catch (e: ApiException) {
            errorMessage = "${e.message}"
        } catch (e: NetworkException) {
            errorMessage = "Network Error: ${e.message}"
        } catch (e: GeneralException) {
            errorMessage = "${e.message}"
        }
    }

    suspend fun handleSaveModelo(
        nome: String,
        tipo: Tipo,
        categoria: Categoria,
    ) {
        val modeloService = ModeloService(RetrofitInstance.modeloApi)
        try {
            val response = modeloService.createModelo(ModeloReq(nome, tipo.id, categoria.id))
            when(response.code()){
                201 -> {
                    Log.d("InformacoesProdutoDialog", "Modelo salvo com sucesso!")
                    confirmarTitulo = "Modelo salvo com sucesso!"
                    imgCasoDeErro = R.mipmap.ic_sucesso
                }
                400 -> {
                    Log.e("InformacoesProdutoDialog", "Erro ao salvar modelo: ${response.errorBody()?.string()}")
                    confirmarTitulo = "Dados não preenchidos corretamente"
                    imgCasoDeErro = R.mipmap.ic_excluir
                }
                409 -> {
                    Log.e("InformacoesProdutoDialog", "Modelo duplicado: ${response.errorBody()?.string()}")
                    confirmarTitulo = "Modelo já cadastrado"
                    imgCasoDeErro = R.mipmap.ic_excluir
                }
                500 -> {
                    Log.e("InformacoesProdutoDialog", "Erro ao salvar modelo: ${response.errorBody()?.string()}")
                    confirmarTitulo = "Erro inesperado ao salvar modelo"
                    imgCasoDeErro = R.mipmap.ic_excluir
                }
                else -> {
                    Log.e("InformacoesProdutoDialog", "Erro ao salvar modelo: ${response.errorBody()?.string()}")
                }
            }

            handleAbrirModalConfirm(
                titulo = confirmarTitulo,
                action = { showSucessDialog = true },
                confirmarTexto = "Ok",
                recusarTexto = "",
                corBtn = Color(0xFF355070)
            )
        } catch (e: ApiException) {
            Log.e("InformacoesProdutoDialog", "ApiException: ${e.message}")
        } catch (e: NetworkException) {
            Log.e("InformacoesProdutoDialog", "NetworkException: ${e.message}")
        } catch (e: GeneralException) {
            Log.e("InformacoesProdutoDialog", "GeneralException: ${e.message}")
        } catch (e: Exception) {
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
                ModalHeaderComponent(onDismissRequest = onDismissRequest, "Novo Modelo")
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
                                    label = "Nome:",
                                    textValue = nome,
                                    onValueChange = { nome = it },
                                    error = showError && nome.isEmpty()
                                )
                                SelectField(
                                    label = "Tipo:",
                                    selectedOption = tipo.nome,
                                    onOptionSelected = { selectedNome ->
                                        val selectedTipo = tiposOptions.find { it.nome == selectedNome }
                                        if (selectedTipo != null) {
                                            tipo = selectedTipo
                                        }
                                    },
                                    options = tiposOptions.map { it.nome },
                                    error = showError && tipo.id <= 0
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                SelectField(
                                    label = "Categoria:",
                                    selectedOption = categoria.nome,
                                    onOptionSelected = { selectedNome ->
                                        val selectedCategoria = categoriasOptions.find { it.nome == selectedNome }
                                        if (selectedCategoria != null) {
                                            categoria = selectedCategoria
                                        }
                                    },
                                    error = showError && categoria.id <= 0,
                                    options = categoriasOptions.map { it.nome }
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
                        onClick = {
                            nome = ""
                            tipo = Tipo(id = 0, nome = "")
                            categoria = Categoria(id = 0, nome = "")
                        },
                        containerColor = Color(0xFF919191),
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    ButtonComponent(
                        titulo = "Salvar",
                        onClick = {
                            if (nome.isNotEmpty() && tipo.nome.isNotEmpty() && categoria.nome.isNotEmpty()) {
                                coroutineScope.launch {
                                        handleSaveModelo(nome, tipo, categoria)
                                        showSucessDialog = true
                                    }
                                } else {
                                    showError = true
                            }
                            },
                        containerColor = Color(0xFF355070),
                    )
                }
            }
        }
    }

    if (showSucessDialog) {
        SucessoDialog(
            titulo = confirmarTitulo,
            onDismiss = {
                showSucessDialog = false
                onDismissRequest()
            },
            onConfirm = {
                showSucessDialog = false
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
fun ModalNovoModeloDialogPreview() {
    // Simulando o comportamento de dismiss para visualização no preview
    ModalNovoModeloDialog(onDismissRequest = {})
}
