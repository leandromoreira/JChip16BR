	cls			;start clean
	spr #0804		;8x8 px sprite
	ldi r0,0		;X coordinate
	ldi r1,0		;Y coordinate
	ldi r2,320		;Max X coordinate
	ldi r3,240		;Max Y coordinate
	ldi r4,0		;Random number
	ldi r5,s_left		;Sprite address
	vblnk			;Wait for vblank
:draw_loop
	rnd r4,1		;Get random number (0 or 1)
	ldi r5,s_left		;Load left sprite
	addi r4,0		;Update zero flag
	jmz draw_continue
	addi r5,32		;Load right sprite
:draw_continue
	drw r0,r1,r5		;Draw sprite on screen
	addi r0,8		;Increase x coordinate
	jme r0,r2,inc_y		;Check if X coresponds with max width
	jmp draw_loop		;There is still empty space to be filled, draw another sprite
:inc_y
	ldi r0,0		;Reset X coordinate
	addi r1,8		;Increase Y coordinate
	jme r1,r3,loop		;Check if Y corresponds to max height
	jmp draw_loop
:loop
	jmp loop		;Endless loop
:s_left
	db 20h
	db 00h
	db 00h
	db 00h
	db 02h
	db 00h
	db 00h
	db 00h
	db 00h
	db 20h
	db 00h
	db 00h
	db 00h
	db 02h
	db 00h
	db 00h
	db 00h
	db 00h
	db 20h
	db 00h
	db 00h
	db 00h
	db 02h
	db 00h
	db 00h
	db 00h
	db 00h
	db 20h
	db 00h
	db 00h
	db 00h
	db 02h
:s_right
	db 20h
	db 00h
	db 00h
	db 00h
	db 00h
	db 00h
	db 00h
	db 02h
	db 00h
	db 00h
	db 00h
	db 20h
	db 00h
	db 00h
	db 02h
	db 00h
	db 00h
	db 00h
	db 20h
	db 00h
	db 00h
	db 02h
	db 00h
	db 00h
	db 00h
	db 20h
	db 00h
	db 00h
	db 02h
	db 00h
	db 00h
	db 00h