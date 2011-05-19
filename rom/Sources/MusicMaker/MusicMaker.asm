;MusicMaker for Chip16
;Shendo, May 2011
;
;r0 - Selector X coordinate (in board coordinates)
;r1 - Selector Y coordinate (in board coordinates)
;r2 - Unused
;r3 - Unused
;r4 - Unused
;r5 - Unused
;r6 - Play timeout
;r7 - Play cursor (0 to 29)
;r8 - Player state (0 - stopped, 1 - playing)
;r9 - PAD1 timeout
;ra - Scratchpad
;rb - Scratchpad
;rc - Scratchpad
;rd - Scratchpad / Sprite memory location
;re - Scratchpad / Sprite X coordinate
;rf - Scratchpad / Sprite Y coordinate
;
;ASCII Index (0xFF is the string terminator):
;00 01 02 03 04 05 06 07 08 09 		HEX
;00 01 02 03 04 05 06 07 08 09		DEC
; 0  1  2  3  4  5  6  7  8  9		SPR
;
;0A 0B 0C 0D 0E 0F 10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F 20 21 22 23		HEX
;10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35		DEC
; A  B  C  D  E  F  G  H  I  J  K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z		SPR
;
;24 25 26 27 28 29 2A 2B 2C 2D 2E 2F 30 31 32 33 34 35 36 37 38 39 3A 3B 3C 3D		HEX
;36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61		DEC
; a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z		SPR
;
;3E 3F 40 41 42 43 44 45 46 47		HEX
;62 63 64 65 66 67 68 69 70 71		DEC
;    !  -  /  ,  .  :  (  )  &		SPR

;GRAPHIC DATA
importbin block_gray.bin 0 32 block_gray			;8x8 pixel gray block
importbin block_green.bin 0 32 block_green			;8x8 pixel green block
importbin selector.bin 0 50 selector				;10x10 selector sprite
importbin cursor.bin 0 32 cursor				;8x8 pixel play cursor
importbin numbers_font.bin 0 320 numbers_font			;Font data (numbers)
importbin capitals_font.bin 0 832 capitals_font			;Font data (capital letters)
importbin lowcase_font.bin 0 832 lowcase_font			;Font data (lowcase letters)
importbin special_font.bin 0 320 special_font			;Font data (special characters)
importbin work_ram.bin 0 62 work_ram				;Part of memory where sequence will be stored


start:					;Start of the application
	cls				;Clear screen for fresh start

	ldi r0, 0			;Default selector X coordinate
	ldi r1, 0			;Default selector Y coordinate

	ldi r9, 0			;No PAD1 timeout by default

	call player_reset		;Set default player values

loop:					;The main game loop
	vblnk				;Wait for VBlank
	cls				;Clear screen

	call process_input		;React to PAD1 keys

	ldi re, 10			;Load string X coordinate
	ldi rf, 10			;Load string Y coordinate
	ldi rd, ascii_maker		;Load string memory location
	call draw_string		;Draw string on screen

	ldi re, 10			;Load string X coordinate
	ldi rf, 188			;Load string Y coordinate
	ldi rd, ascii_dpad		;Load string memory location
	call draw_string		;Draw string on screen

	ldi re, 10			;Load string X coordinate
	ldi rf, 200			;Load string Y coordinate
	ldi rd, ascii_start		;Load string memory location
	call draw_string		;Draw string on screen

	ldi re, 10			;Load string X coordinate
	ldi rf, 212			;Load string Y coordinate
	ldi rd, ascii_a			;Load string memory location
	call draw_string		;Draw string on screen

	ldi re, 10			;Load string X coordinate
	ldi rf, 224			;Load string Y coordinate
	ldi rd, ascii_b			;Load string memory location
	call draw_string		;Draw string on screen


	call draw_blocks		;Draw "playfield"
	call draw_selector		;Draw selector sprite
	call draw_cursor		;Draw cursor sprite

	addi r6, 0			;Update flags
	jmz sound_engine		;No timeout, play sound

	subi r6, 1			;Decrease play timeout

	jmp loop			;Draw another frame

:sound_engine
	addi r8, 0			;Update flags
	jmz loop			;Player is inactive, draw another frame

	call play_sound			;Play pre-set sounds

	addi r7, 1			;Increase play cursor
	ldi r6, 10			;Set play timeout
	
	mov ra, r7			;Move play cursor to scratchpad
	subi ra, 31			;Check if max value has been reached
	jmz sound_engine_stop		;Stop playing sounds

	jmp loop			;Draw another frame

:sound_engine_stop
	call player_reset		;Set default player values

	jmp loop			;Draw another frame

;SUBROUTINES

:player_reset				;Set default values
	snd0				;Stop playing sounds
	ldi r6, 0			;No play timeout
	ldi r7, 0			;Default play cursor
	ldi r8, 0			;Playing is stopped by default

	ret				;Return from a subroutine

:play_sound				;Play a sound of the specific frequency
	mov ra, r7			;Copy cursor value to scratchpad
	muli ra, 2			;Convert to work RAM offset

	ldi rc, work_ram		;Load work RAM address
	add rc, ra			;Apply precalculated offset
	
	ldm rb, rc			;Load sound index value
	addi rb, 0			;Update flags
	jmz fret			;No sound should be played, break subroutine

	subi rb, 1			;Decrease sound index
	jmz play_sound_500		;Index was 1, play 500 Hz tone

	subi rb, 1			;Decrease sound index
	jmz play_sound_1000		;Index was 2, play 1000 Hz tone

	subi rb, 1			;Decrease sound index
	jmz play_sound_1500		;Index was 3, play 1500 Hz tone

	ret				;Return from a subroutine

:play_sound_500
	snd1 200			;Play 500 Hz tone for 200 ms
	ret				;Return from a subroutine

:play_sound_1000
	snd2 200			;Play 1000 Hz tone for 200 ms
	ret				;Return from a subroutine

:play_sound_1500
	snd3 200			;Play 1500 Hz tone for 200 ms
	ret				;Return from a subroutine

:process_input				;Process PAD1 keys
	addi r9, 0			;Update flags
	jmz process_input_up		;Timeout is zero, read keys

	subi r9, 1			;Decrease timeout
	ret				;Return from a subroutine


:process_input_up
	ldm ra, #FFF0			;Load PAD1 status
	andi ra, 1			;Isolate up key
	jmz process_input_down		;Up key is not pressed, proceed to next key

	addi r1, 0			;Update flags
	jmz process_input_down		;Minimum offset reached, proceed to next key

	subi r1, 1			;Decrease selector Y coordinate
	ldi r9, 4			;Load 4 frames timeout

:process_input_down
	ldm ra, #FFF0			;Load PAD1 status
	andi ra, 2			;Isolate down key
	jmz process_input_left		;Down key is not pressed, proceed to next key

	mov rf, r1			;Copy Y coordinate to scratchpad
	subi rf, 2			;Remove maximum offset
	jmz process_input_left		;Maximum offset reached, proceed to next key

	addi r1, 1			;Increase selector Y coordinate
	ldi r9, 4			;Load 4 frames timeout

:process_input_left
	ldm ra, #FFF0			;Load PAD1 status
	andi ra, 4			;Isolate left key
	jmz process_input_right		;Left key is not pressed, proceed to next key

	addi r0, 0			;Update flags
	jmz process_input_right		;Minimum offset reached, proceed to next key

	subi r0, 1			;Decrease selector X coordinate
	ldi r9, 4			;Load 4 frames timeout

:process_input_right
	ldm ra, #FFF0			;Load PAD1 status
	andi ra, 8			;Isolate right key
	jmz process_input_a		;Right key is not pressed, proceed to next key

	mov rf, r0			;Copy X coordinate to scratchpad
	subi rf, 29			;Remove maximum offset
	jmz process_input_a		;Maximum offset reached, proceed to next key

	addi r0, 1			;Increase selector X coordinate
	ldi r9, 4			;Load 4 frames timeout

:process_input_a
	ldm ra, #FFF0			;Load PAD1 status
	andi ra, 64			;Isolate A key
	jmz process_input_b		;A key is not pressed, proceed to next key

	mov rc, r0			;Move selector X coordinate to scratchpad
	muli rc, 2			;Convert to memory offset

	ldi rd, work_ram		;Load work RAM memory address
	add rd, rc			;Add precalculated offset

	mov re, r1			;Move selector Y coordinate to scratchpad
	addi re, 1			;Convert to sound index value
	stm re, rd			;Store value to memory

	ldi r9, 4			;Load 4 frames timeout

:process_input_b
	ldm ra, #FFF0			;Load PAD1 status
	andi ra, 128			;Isolate B key
	jmz process_input_start		;B key is not pressed, proceed to next key

	mov rc, r0			;Move selector X coordinate to scratchpad
	muli rc, 2			;Convert to memory offset

	ldi rd, work_ram		;Load work RAM memory address
	add rd, rc			;Add precalculated offset

	ldi re, 0			;Load "cleared" value
	stm re, rd			;Store cleared value to memory

	ldi r9, 4			;Load 4 frames timeout

:process_input_start
	ldm ra, #FFF0			;Load PAD1 status
	andi ra, 32			;Isolate start key
	jmz fret			;start key is not pressed, break subroutine

	ldi r9, 10			;Load 10 frames timeout

	xori r8, 1			;Toggle player state
	jmz player_reset		;If the player is not running, reset values

	ret				;Return from a subroutine

:draw_cursor				;Draw cursor on screen
	spr #0804			;Set 8x8 pixel sprites
	
	mov ra, r7			;Copy play cursor to scratchpad

	addi ra, 0			;Update flags
	jmz fret			;Skip drawing if the cursor is zero

	muli ra, 10			;Convert to X offset
	ldi rb, 92			;Cursor Y coordinate

	drw ra, rb, cursor		;Draw sprite on screen

	ret				;Return from a subroutine

:draw_selector				;Draw selector sprite on screen
	spr #0A05			;Set 10x10 pixel sprites

	mov ra, r0			;Copy selector X coordinate
	mov rb, r1			;Copy selector Y coordinate

	muli ra, 10			;Convert to board coordinates
	muli rb, 10			;Convert to board coordinates
	
	addi ra, 9			;Add offset
	addi rb, 101			;Add offset

	drw ra, rb, selector		;Draw sprite on screen

	ret				;Return from a subroutine

:draw_blocks				;Draw set of gray or green blocks
	spr #0804			;Set 8x8 pixel sprites

	ldi ra, 300			;Default X coordinate
	ldi rb, 102			;Default Y coordinate
	ldi rc, work_ram		;Load work RAM address

	addi rc, 58			;Increase Work RAM offset

:draw_blocks_1
	drw ra, rb, block_green		;Draw green sprite on screen

	ldm rd, rc			;Load data from memory
	subi rd, 1			;Remove 500 Hz offset
	jmz draw_blocks_2		;Offset matched, skip drawing gray sprite

	drw ra, rb, block_gray		;Draw gray sprite on screen
	

:draw_blocks_2
	addi rb, 10			;Increase Y coordinate
	drw ra, rb, block_green		;Draw green sprite on screen

	ldm rd, rc			;Load data from memory
	subi rd, 2			;Remove 1000 Hz offset
	jmz draw_blocks_3		;Offset matched, skip drawing gray sprite

	drw ra, rb, block_gray		;Draw gray sprite on screen

:draw_blocks_3
	addi rb, 10			;Increase Y coordinate
	drw ra, rb, block_green		;Draw green sprite on screen

	ldm rd, rc			;Load data from memory
	subi rd, 3			;Remove 1500 Hz offset
	jmz draw_decrease		;Offset matched, skip drawing gray sprite

	drw ra, rb, block_gray		;Draw gray sprite on screen

:draw_decrease
	subi rb, 20			;Restore Y coordinate

	subi rc, 2			;Decreser work RAM offset

	subi ra, 10			;Decrease X offset
	jmz fret			;Break subroutine if all blocks have been drawn

	jmp draw_blocks_1		;Draw another block

:draw_string				;Draw string on screen
	spr #0804			;Set 8x8 pixel sprites
	ldm ra, rd			;Load characted from memory
	andi ra, #FF			;Only the lo byte is needed

	mov rb, ra			;Copy data to scratchpad
	subi rb, 255			;Remove terminator
	jmz fret			;Terminator reached, break subroutine

	mov rb, ra			;Copy data to scratchpad
	muli rb, 32			;Each character is 32 bytes long
	addi rb, numbers_font		;Apply offset to font address

	drw re, rf, rb			;Draw 8x8 character on the set coordinates

	addi rd, 1			;Increase memory offset
	addi re, 9			;Increase X coordinate

	jmp draw_string

:fret					;Subroutine return function
	ret				;Return from a subroutine

;STRING DATA

:ascii_author				;Author's name in standard ASCII
	db #53		;S
	db #68		;h
	db #65		;e
	db #6E		;n
	db #64		;d
	db #6F		;o
	db #00		;/0

:ascii_maker
	db 22		;M
	db 56		;u
	db 54		;s
	db 44		;i
	db 38		;c
	db 62		; 
	db 48		;m
	db 36		;a
	db 46		;k
	db 40		;e
	db 53		;r
	db 255		;/0

:ascii_dpad
	db 13		;D
	db 64		;-
	db 25		;P
	db 10		;A
	db 13		;D
	db 62		; 
	db 64		;-
	db 62		; 
	db 22		;M
	db 50		;o
	db 57		;v
	db 40		;e
	db 62		; 
	db 54		;s
	db 40		;e
	db 47		;l
	db 40		;e
	db 38		;c
	db 55		;t
	db 50		;o
	db 53		;r
	db 255		;/0

:ascii_start
	db 28		;S
	db 29		;T
	db 10		;A
	db 27		;R
	db 29		;T
	db 62		; 
	db 64		;-
	db 62		; 
	db 25		;P
	db 47		;l
	db 36		;a
	db 60		;y
	db 65		;/
	db 28		;S
	db 55		;t
	db 50		;o
	db 51		;p
	db 255		;/0

:ascii_a
	db 10		;A
	db 62		; 
	db 64		;-
	db 62		; 
	db 10		;A
	db 38		;c
	db 55		;t
	db 44		;i
	db 57		;v
	db 36		;a
	db 55		;t
	db 40		;e
	db 62		; 
	db 37		;b
	db 47		;l
	db 50		;o
	db 38		;c
	db 46		;k
	db 255		;/0

:ascii_b
	db 11		;B
	db 62		; 
	db 64		;-
	db 62		; 
	db 14		;E
	db 53		;r
	db 36		;a
	db 54		;s
	db 40		;e
	db 62		; 
	db 38		;c
	db 50		;o
	db 47		;l
	db 56		;u
	db 48		;m
	db 49		;n
	db 255		;/0