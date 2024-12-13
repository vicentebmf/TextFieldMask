# TextFieldMask
Implementação da biblioteca Maskara para Jetpack  Compose
O aplicativo TextFieldMask é uma implementação em Kotlin da biblioteca “Maskara” de Fatih Santalu, disponível em https://github.com/santalu/maskara). 
Ele faz o mascaramento de componentes TextField, do jejtpack compose, passando um objeto da classe VisualTransformation contendo as opções de mascaramento.
O app possui uma function compose contendo três TextField: phone,  cpf e cnpj, cada qual com mascaramento adequado à natureza da informação.
Além do mascaramento, o app faz uma validação simples dos caracteres aceitos no TextField. Para fins de demonstração o phone e cpf foram configurados para aceitarem apenas números, enquanto o cnpj foi configurado para aceitar apenas letras e números.
Existem 3 formatos de apresentação da variável mascarada: COMPLETABLE, NORMAL e PERSISTENT. 
A visualização mascarada não interfere no conteúdo sem máscara do campo digitado.
