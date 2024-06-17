.data
pi: .asciiz ""
pi2: .word 0
pi3: .word 0
x24: .asciiz ""
x275: .word 0
x2: .float 0.0
otra: .word 0
ch24: .asciiz ""
flag : .word 0
cont : .word 0
cant : .word 0
jump : .word 0
init : .word 0
String1: .asciiz "'3'"
String2: .asciiz "'!#'"
String3: .asciiz "'un case'"
String4: .asciiz "'a'"
.text
.globl main
main:

begin_globals_:

begin_func_func1:
addi $sp, $sp, -4
li $t0, 0x40800000
mtc1 $t0, $f0
s.s $f0, 0($sp)
addi $sp, $sp, -4
lw $t0, 4($sp)
mtc1 $t0, $f4
s.s $f4, 0($sp)
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
addi $sp, $sp, -4
lw $t0, 4($sp) 
lw $t1, 8($sp) 
add $t2, $t0, $t1
sw $t2, 0($sp)
addi $sp, $sp, -4
li $v0, 5
sw $v0, 0($sp)
addi $sp, $sp, -4
lw $t0, 4($sp) 
lw $t1, 8($sp) 
add $t2, $t0, $t1
sw $t2, 0($sp)
lw $t0, -8($sp)
li $v0, 1
move $a0, $t0
syscall
lw $t0, -12($sp)
li $v0, 1
move $a0, $t0
syscall
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)

begin_switch_1:
addi $sp, $sp, -4
addi $sp, $sp, -4
li $v0, 0
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
lw $t0, 16($sp)
lw $t1, 12($sp)
beq $t0, $t1, begin_case_11
lw $t0, 8($sp)
lw $t1, -4($sp)
beq $t0, $t1, begin_case_11
j end_case_11

begin_case_11:
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
lw $t0, -20($sp)
li $v0, 1
move $a0, $t0
syscall

end_case_11:
addi $sp, $sp, -4
li $v0, 2
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
lw $t0, 24($sp)
lw $t1, 20($sp)
beq $t0, $t1, begin_case_12
lw $t0, 16($sp)
lw $t1, -16($sp)
beq $t0, $t1, begin_case_12
j end_case_12

begin_case_12:
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 10
sw $v0, 0($sp)
addi $sp, $sp, -4
lw $t0, 24($sp) 
lw $t1, 28($sp) 
add $t2, $t0, $t1
sw $t2, 0($sp)
addi $sp, $sp, -4
j end_switch_1

end_case_12:
addi $sp, $sp, -4
li $v0, -1
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 10
sw $v0, 0($sp)
addi $sp, $sp, -4
lw $t0, 24($sp) 
lw $t1, 28($sp) 
mul $t2, $t0, $t1
sw $t2, 0($sp)
addi $sp, $sp, -4
li $v0, -1
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
addi $sp, $sp, -4
lw $t0, 24($sp) 
lw $t1, 28($sp) 
mul $t2, $t0, $t1
sw $t2, 0($sp)
addi $sp, $sp, -4
lw $t0, 16($sp) 
lw $t1, 28($sp) 
mul $t2, $t0, $t1
sw $t2, 0($sp)
addi $sp, $sp, -4

begin_default_11:

end_switch_1:
addi $sp, $sp, -4
li $v0, 3
sw $v0, 0($sp)

begin_switch_2:
addi $sp, $sp, -4
addi $sp, $sp, -4
li $v0, 0
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 10
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
lw $t0, 32($sp)
lw $t1, 28($sp)
beq $t0, $t1, begin_case_21
lw $t0, 24($sp)
lw $t1, 12($sp)
beq $t0, $t1, begin_case_21
j end_case_21

begin_case_21:
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
lw $t0, -36($sp)
li $v0, 1
move $a0, $t0
syscall

end_switch_2:

begin_func_func2:
addi $sp, $sp, -4
li $v0, 7
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 5
sw $v0, 0($sp)
addi $sp, $sp, -4
lw $t0, -128($sp)
lw $t1, -124($sp)
sgt $t2, $t0, $t1
sw $t2, 0($sp)
lw $t0, -124($sp)
bne $t0, $zero, begin_if_1
j begin_else_1

begin_if_1:
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
lw $t0, 124($sp)
li $v0, 1
move $a0, $t0
syscall
j begin_end_if_1

begin_else_1:

begin_end_if_1:

begin_while_1:
addi $sp, $sp, -4
li $v0, 12
sw $v0, 0($sp)
addi $sp, $sp, -4
li $v0, 7
sw $v0, 0($sp)
addi $sp, $sp, -4
lw $t0, -128($sp)
lw $t1, -124($sp)
slt $t2, $t0, $t1
sw $t2, 0($sp)
lw $t0, -124($sp)
bne $t0, $zero, begin_while_block_1
j begin_end_while_1

begin_while_block_1:
addi $sp, $sp, -4
li $v0, 1
sw $v0, 0($sp)
lw $t0, 124($sp)
li $v0, 1
move $a0, $t0
syscall
j begin_while_1

begin_end_while_1:

begin_func_func5:

begin_func_func3:

#---------------------Macros---------------------

#entrada: $a0, debe ser una etiqueta.
#salida: $a0.
print:
li $v0, 4          # Cargar el valor 4 en $v0 para la llamada al sistema para imprimir un string
syscall            # Llamar al sistema para imprimir un string
jr $ra             # Retornar al registro de dirección de retorno

#entrada: $t0, debe ser un numero entero.
#salida: $a0
print_entero:
li $v0, 1         # Syscall para imprimir un entero
move $a0, $t0     # Mueve el número a imprimir a $a0, entrada $t0
syscall
jr $ra 