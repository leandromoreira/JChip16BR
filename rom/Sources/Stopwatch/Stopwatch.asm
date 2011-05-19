;Stopwatch demo for Chip16
;Shendo, May 2011
;
;r0 - Minutes
;r1 - Seconds
;r2 - Tenths
;r3 - Timer state (0 - not running, 1 - running)
;r4 - Pressed pad flag (0 - not pressed, 1 - pressed)
;r5 - Unused
;r6 - Unused
;r7 - Unused
;r8 - Unused
;r9 - Unused
;ra - Scratchpad
;rb - Scratchpad
;rc - Scratchpad
;rd - Sprite memory location
;re - Sprite X coordinate
;rf - Sprite Y coordinate
;
;ASCII Index (0xFF is the string terminator):
;00 01 02 03 04 05 06 07 08 01 0A 0B 0C 0D 0E 0F 10 11 12 13	HEX
;00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19	DEC
; 0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F  G  H  I  J	SPR
;
;14 15 16 17 18 19 1A 1B 1C 1D 1E 1F 20 21 22 23 24 25 26 27	HEX
;20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39	DEC
; K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z     -  /  :	SPR

;GRAPHIC DATA
importbin font.bin 0 2000 font		;Font data

start:					;Start of the application
	cls				;Clear screen for fresh start

	ldi r3, 0			;Timer is not running by default
	ldi r4, 0			;Clear pressed flag
	
	call reset_timer		;Set default timer values

loop:					;The main game loop
	ldi ra, 6			;Load number of frames to wait
	call wait_frames		;Wait for required number of frames

	cls				;Clear screen

	mov ra, r2			;Copy tenths to scratchpad
	ldi rd, string_data_2		;Load tenths string address	
	call bcd_to_mem			;Store tenths BCD data to memory

	mov ra, r1			;Copy seconds to scratchpad
	ldi rd, string_data_1		;Load second string address	
	call bcd_to_mem			;Store second BCD data to memory

	mov ra, r0			;Copy minutes to scratchpad
	ldi rd, string_data_0		;Load minutes string address	
	call bcd_to_mem			;Store minutes BCD data to memory

	ldi re, 10			;Load string X coordinate
	ldi rf, 10			;Load string Y coordinate
	ldi rd, string_data_0		;Load string memory location
	call draw_string		;Draw string on screen

	ldi re, 10			;Load string X coordinate
	ldi rf, 200			;Load string Y coordinate
	ldi rd, string_data_3		;Load string memory location
	call draw_string		;Draw string on screen

	ldi re, 10			;Load string X coordinate
	ldi rf, 220			;Load string Y coordinate
	ldi rd, string_data_4		;Load string memory location
	call draw_string		;Draw string on screen

	addi r3, 0			;Update flags
	jmz loop			;Timer is not running, skip incrementing

	addi r2, 10			;Increase tenths

	mov ra, r2			;Copy tenths to scratchpad
	subi ra, 100			;Check maximum value
	jmz increment_seconds		;Maximum value reached, inscrease seconds
	jmp loop			;Draw another frame

:increment_seconds
	ldi r2, 0			;Reset tenths
	addi r1, 1			;Increment seconds

	mov ra, r1			;Copy seconds to scratchpad
	subi ra, 60			;Check maximum value
	jmz increment_minutes		;Maximum value reached, inscrease minutes
	jmp loop			;Draw another frame

:increment_minutes
	ldi r1, 0			;Reset seconds
	addi r0, 1			;Increment minutes

	mov ra, r0			;Copy minutes to scratchpad
	subi ra, 60			;Check maximum value
	jmz reset_timer_call		;Maximum value reached, reset timer
	jmp loop			;Draw another frame

:reset_timer_call
	call reset_timer		;Reset calues to defaults

	jmp loop			;Draw another frame


;SUBROUTINES

:process_input				;Process pressed keys
	ldm rb, #FFF0			;Load PAD1 status
	
	addi rb, 0			;Update flags
	jmz clear_pad_flag		;No keys are pressed, clear flag
	jmp check_pad_flag		;React to pressed keys	

:clear_pad_flag				;Reset pad flag status
	ldi r4, 0			;Clear pad flag

:check_pad_flag
	addi r4, 0			;Update flags
	jmz process_a			;Process pressed keys
	
	ret				;Return from a subroutine

:process_a
	ldm rb, #FFF0			;Load PAD1 status
	andi rb, 64			;Isolate A key
	jmz process_b			;A is not pressed, ckeck B

	xori r3, 1			;Toggle running status
	ldi r4, 1			;Raise pad flag

	ret				;Return from a subroutine

:process_b
	ldm rb, #FFF0			;Load PAD1 status
	andi rb, 128			;Isolate B key
	jmz fret			;B is not pressed, break subroutine

	call reset_timer		;Reset timer to default values
	ldi r4, 1			;Raise pad flag

	ret				;Return from a subroutine

:reset_timer
	ldi r0, 0			;Load default minutes
	ldi r1, 0			;Load default seconds
	ldi r2, 0			;Load default tenths

	ret				;Return from a subroutine

:wait_frames				;Wait for the required number of frames
	addi ra, 0			;Update flags
	jmz fret			;Counter is zero, break subroutine

	vblnk				;Wait for VBlank
	call process_input		;React to PAD1 keys
	subi ra, 1			;Decrement counter
	
	jmp wait_frames			;Do another loop


:bcd_to_mem				;Convert number to BCD and store it in memory
	mov rb, ra			;Copy numeric value
	divi rb, 10			;Isolate tens

	mov rc, rb			;Copy tens to scratchpad
	muli rc, 10			;Convert to tens
	sub ra, rc			;Get ones
	
	shl ra, 8			;Move ones to lo byte
	or rb, ra			;Add tens to value

	stm rb, rd			;Store string data to memory

	ret				;Return from subroutine


:draw_string				;Draw string on screen
	spr #0A05			;Set 10x10 pixel sprites
	ldm ra, rd			;Load characted from memory
	andi ra, #FF			;Only the lo byte is needed

	mov rb, ra			;Copy data to scratchpad
	subi rb, 255			;Remove terminator
	jmz fret			;Terminator reached, break subroutine

	mov rb, ra			;Copy data to scratchpad
	muli rb, 50			;Each character is 50 bytes long
	addi rb, font			;Apply offset to font address

	drw re, rf, rb			;Draw 10x10 character on the set coordinates

	addi rd, 1			;Increase memory offset
	addi re, 12			;Increase X coordinate

	jmp draw_string			;Draw another character

:fret					;Subroutine return function
	ret				;Return from a subroutine

;STRING DATA

:string_data_0				;Minutes
	db 00
	db 00
	db 39
:string_data_1				;Seconds
	db 00
	db 00
	db 39
:string_data_2				;Tenths
	db 00
	db 00
	db 255

:string_data_3				;A - START/STOP
	db 10
	db 36
	db 37
	db 36
	db 28
	db 29
	db 10
	db 27
	db 29
	db 38
	db 28
	db 29
	db 24
	db 25
	db 255

:string_data_4				;B - RESET
	db 11
	db 36
	db 37
	db 36
	db 27
	db 14
	db 28
	db 14
	db 29
	db 255