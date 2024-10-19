package com.example.mystockapp.modais

import ProductTable
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mystockapp.ui.theme.MyStockAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystockapp.R
import com.example.mystockapp.modais.componentes.ButtonComponent
import com.example.mystockapp.modais.viewModels.AddProdEstoqueViewModel
import com.example.mystockapp.modais.viewModels.AddProdEstoqueViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AddProdutoEstoque(onDismissRequest: () -> Unit, context: Context = androidx.compose.ui.platform.LocalContext.current) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSucessoDialog by remember { mutableStateOf(false) }
    var imgCasoDeErro by remember { mutableStateOf<Int?>(null) }
    var actionToPerform by remember { mutableStateOf<suspend () -> Unit>({}) }
    var sucessoDialogTitulo by remember { mutableStateOf<String?>(null) }
    var tituloFinalizacaoDialog by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val sharedPreferences = context.getSharedPreferences("MyStockPrefs", Context.MODE_PRIVATE)
    val idLoja = sharedPreferences.getInt("idLoja", -1)

    val viewModel: AddProdEstoqueViewModel = viewModel(
        factory = AddProdEstoqueViewModelFactory(idLoja = idLoja)
    )

    // contexto local (a tela atual)
    val contexto = LocalContext.current

    fun handleAbrirModalConfirm(
        titulo: String,
        action: suspend () -> Unit,
        confirmarTexto: String,
        recusarTexto: String,
        corBtn: Color
    ) {
        showConfirmDialog = true
        actionToPerform = action
        sucessoDialogTitulo = titulo
    }

    LaunchedEffect(Unit) {
        viewModel.fetchProdutos()
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(8.dp)
                .border(0.dp, Color.Transparent, RoundedCornerShape(10.dp))
        ) {
            ModalHeaderComponent(onDismissRequest = onDismissRequest, stringResource(id = R.string.add_produto_estoque_title))
            Spacer(modifier = Modifier.height(6.dp))

            if (errorMessage != null) {
                Text(text = errorMessage ?: "", color = Color.Red)
            } else {
                ProductTable(
                    products = viewModel.produtos,
                    viewModel = viewModel
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ButtonComponent(
                    titulo = stringResource(id = R.string.limpar_button),
                    onClick = { viewModel.limparProdutos() },
                    containerColor = Color(0xFF919191),
                )
                Spacer(modifier = Modifier.width(24.dp))
                ButtonComponent(
                    titulo = stringResource(id = R.string.adicionar_button),
                    onClick = {
                        handleAbrirModalConfirm(
                            contexto.getString(R.string.confirmacao_dialog_title),
                            { viewModel.handleAdicionarProdutos() },
                            contexto.getString(R.string.adicionar_button),
                            "Cancelar",
                            Color(0xFF355070)
                        )
                    },
                    containerColor = Color(0xFF355070),
                )
            }
        }
    }

    if (showConfirmDialog) {
        ConfirmacaoDialog(
            titulo = sucessoDialogTitulo ?: "",
            confirmarBtnTitulo = stringResource(id = R.string.adicionar_button),
            recusarBtnTitulo = "Cancelar",
            imagem = painterResource(id = R.mipmap.ic_editar),
            onConfirm = {
                coroutineScope.launch {
                    viewModel.handleAdicionarProdutos()
                    showConfirmDialog = false
                    showSucessoDialog = true
                }
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }

    if (showSucessoDialog) {
        SucessoDialog(
            titulo = tituloFinalizacaoDialog ?: stringResource(id = R.string.sucesso_dialog_title),
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
            btnConfirmTitulo = stringResource(id = R.string.ok_button)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyStockAppTheme() {
        AddProdutoEstoque(onDismissRequest = {})
    }
}