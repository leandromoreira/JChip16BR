importbin k9.bin 0 5400 k0
importbin k8.bin 0 5400 k1
importbin k7.bin 0 5400 k2
importbin k6.bin 0 5400 k3
importbin k5.bin 0 5400 k4
importbin k4.bin 0 5400 k5
importbin k3.bin 0 5400 k6
importbin k2.bin 0 5400 k7
importbin k1.bin 0 5400 k8
importbin k0.bin 0 5400 k9

init:
	cls
	spr 0x5A3C			; 120x90
	bgc 0x2				; gray
	ldi r0, 10
	ldi r2, 100
	ldi r3, 75
loop:
	subi r0, 1
	jmc end
	mov r1, r0
	muli r1, 5400
	addi r1, k0
	drw r2, r3, r1
wait:
	ldi r4, 10
wait_loop:
	subi r4, 1
	jmc wait_end
	vblnk
	jmp wait_loop
wait_end:
	jmp loop
end:
	ldi r4, 30
end_loop:
	subi r4, 1
	jmc init
	vblnk
	jmp end_loop