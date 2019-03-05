# Micro Scripts Imperativos Baseados em LISP e Português.

**Extensão:** *.slp

### Definições:

* A Linguagem possui escopo de **função e global**.
* Tipagem **forte e estática**.
* Estruturas de controle **if, while, for**.
* LISP like (Somente sintaticamente).

### Vocabulário

**Palavras Reservadas (PR)**: 

|Palavra |Equivalente   |
|--------|--------------|
|fun     |function      |
|int     |int           |
|real    |double        |
|texto   |string        |
|logico  |bool, boolean |
|nada    |void          |
|enquanto|while         |
|se      |if            |
|senao   |else          |
|retorna |return        |

**Delimitadores (DE)**:

|Símbolo|Descrição|
|-------|---------|
|(      |Abertura do escopo|
|)      |Fechamento do escopo|
|[||
|]||

**Operadores (OP)**:

|Símbolo|Descrição|
|-------|---------|
|`=`    |Atribuição|
|`+`    |Soma|
|`-`    |Substração|
|`*`    |Multiplicação|
|`/`    |Divisão|
|`&&`   |Comparação **AND**|
|<code>&#124;&#124;</code>| Comparação **OR**|
|`.`    |Concatenar|
|`==`   |Comparacão é igual á|
|`!=`   |Comparação é diferente de|
|`>`    |Maior que|
|`<`    |Menor que|
|`>=`   |Maior ou igual á|
|`<=`   |Menor ou igual á|

**Constante Literal (CL)**:

|Constante Literal | Expressão Regular | Exemplo |
|------------------|-------------------|-----------|
|ID Identificador  | l(l+d)* | myFunction, resultado|
|CL Inteira (CLI)  | dd* | 10, 29, 34550
|CL Real (CLR)     | dd*.dd* | 1.23, 0.0003, 1.234
|CL Lógica (CLL)   | V <code>&#124;</code> F | true or false
|CL Separador (CLS)| \"(l+d+BLANKSPACE)\" | `(_fun_soma_[_(_int_a_)_(_int_b_)_]` _ = BLANKSPACE

**Exemplo programas**:

```
( fun soma [ ( int a ) , ( int b ) ] : int
     ( int aux )
     ( = aux ( + a ( sub a b) ) )
     ( ret aux )
)

( texto s1 )
( = s1 ( "soma = " ( soma 3 4 ) ) )
( mostra s1 )

```

```
( fun teste [ ] : nada
    ( se ( < a1 100 )
        ( ( = a1 100 ) )
        ( ( = a1 20 ) )
    )
)
```

```
( fun teste2 [ ] : nada
    ( enquanto ( a > 10 )
        ( = a 100 )
        ( = b 20 )
    )
)
```