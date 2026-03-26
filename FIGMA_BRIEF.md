# Figma AI Design Brief — Block Snake

---

## 1. Art Style & Color Palette

Block Snake lives in a **neon-lit retro arcade** world — pixel-edged geometry meets glowing circuit-board grids. The visual language blends 80s arcade cabinet energy with modern neon glow post-processing: crisp hard edges on the snake segments and bricks, with soft bloom halation on every glowing surface. Backgrounds feel like deep-space PCBs — dark navy grids with faint scanline overlays and subtle CRT vignette. Bricks have a chunky 3D bevel with crack-particle texture, implying weight and destruction. The overall mood is tense, satisfying, and retro-cool.

**Primary Palette:**
- `#0A0E1A` — Deep void navy (background base)
- `#1B2A4A` — Grid-line midnight blue (secondary background)
- `#00FFB2` — Electric snake green (primary character color, glow source)
- `#FF4B4B` — Brick crimson (destructible walls, danger cue)

**Accent:**
- `#FFD700` — Pellet gold (collectible highlight)
- `#A259FF` — Smash-effect violet (shockwave / break FX)

**Font Mood:** Chunky pixel-display typeface (e.g. PressStart2P or Kongtext). All caps, monospaced, with a slight outer glow in `#00FFB2` for scores and a `#FF4B4B` glow for warnings.

---

## 2. App Icon — icon_512.png (512×512px)

**Canvas:** 512×512px square, full bleed, no rounded corners applied in source (store applies mask).

**Background:** Deep radial gradient from `#0A0E1A` at center expanding to `#05060F` at corners, with a subtle dark teal inner ring glow suggesting a monitor bezel. Faint diagonal scanline texture overlaid at 8% opacity.

**Central Symbol:** A top-down pixel-art snake curled in a tight clockwise spiral — three chunky segments visible, rendered in `#00FFB2` with a bright white core highlight on each segment edge. The snake's head faces right and is mid-impact with a single crimson brick (`#FF4B4B`) positioned at 2-o'clock — brick showing a lightning-bolt crack splitting it diagonally. A starburst shockwave in `#A259FF` with 12 rays radiates from the collision point, fading to transparent within 80px.

**Glow / Shadow:** The snake emits a two-layer bloom: inner tight glow `#00FFB2` at 20px spread, outer soft halo `#00FFB2` at 60px spread, 40% opacity. The broken brick has a hot-white impact flash at the crack center. A small golden pellet (`#FFD700`) sits in the lower-left quadrant of the spiral, glowing softly with a 15px warm halo.

**Mood:** Punchy, iconic, instantly readable at 60px — conveys "snake + destruction" in one glance.

---

## 3. UI Screens (480×854 portrait)

---

### MenuScreen

**A) BACKGROUND IMAGE**

Full-bleed dark background using a vertical gradient: `#05060F` top → `#0A1628` bottom. A perspective-foreshortened grid of square cells recedes toward a vanishing point at 50% width, 38% height — grid lines in `#1B3060` at 1px, glowing faintly. A large decorative snake silhouette (just the body curve, no head detail) sweeps diagonally from lower-left to upper-right as a dark teal shape (`#0D2A3A`) at 30% opacity, giving the background depth. Three empty rounded-rectangle banner frames float center-screen: one wide tall frame (main card, ~300×220px centered at Y=370) and two smaller flanking pill shapes — all outlined in `#00FFB2` at 2px with an inner glow, completely hollow inside. Ambient particle dots — tiny `#00FFB2` and `#FFD700` specks — scatter at random across the grid, ~40 particles, pulsing size 2–4px.

**B) BUTTON LAYOUT**

```
TITLE LABEL "BLOCK SNAKE" | top-Y=120px | x=centered | size=340x64
PLAY            | top-Y=380px | x=centered | size=260x56
HOW TO PLAY     | top-Y=454px | x=centered | size=260x48
LEADERBOARD     | top-Y=516px | x=centered | size=260x48
SETTINGS        | top-Y=578px | x=centered | size=260x48
SCORE LABEL     | top-Y=700px | x=centered | size=300x36
```

---

### GameScreen

**A) BACKGROUND IMAGE**

Dark void base `#07091A` with a tight isometric-style dot grid (`#152040`, 2px dots every 32px) filling the entire canvas — this is the play-field floor texture. Left and right edges have a very subtle vignette gradient darkening inward 40px to help focus attention on center play. A faint horizontal scanline pattern at 6% opacity overlays the full screen (CRT nostalgia effect). At the top, a horizontal decorative banner strip (Y=0 to Y=80px) in a slightly lighter shade `#0D1530` separates the HUD zone from the play area visually — no text or icons, just a clean dark band with a 1px bottom border in `#00FFB2` at 50% opacity. At the very bottom (Y=800 to Y=854), a matching dark strip provides visual grounding.

**B) BUTTON LAYOUT**

```
SCORE VALUE     | top-Y=22px | x=centered | size=200x36
PAUSE BUTTON    | top-Y=20px | x=right@16px | size=64x44
SEGMENT COUNT   | top-Y=22px | x=left@16px | size=140x36
```

---

### GameOverScreen

**A) BACKGROUND IMAGE**

The same game-field dot-grid background from GameScreen, now heavily desaturated and darkened — overlay `#000000` at 55% opacity creates a dim blur effect. A centered dramatic red-tinted vignette radiates from center: transparent at center expanding to `#1A0000` at corners, reinforcing the "you died" mood. Center canvas holds a large decorative panel frame — a hexagonal-clipped rectangle approximately 380×440px centered at X=240, Y=440 — outlined in `#FF4B4B` at 3px with a crimson inner glow (24px spread, 35% opacity), hollow interior. Above the frame, two abstract crack-line decorations in `#FF4B4B` splay outward like shattered glass (purely decorative, ~80px wide each, placed at Y=180). Tiny debris particle shapes — small squares and triangles in `#A259FF` and `#FF4B4B` — scatter from the crack point outward, 25–30 particles.

**B) BUTTON LAYOUT**

```
GAME OVER LABEL | top-Y=160px | x=centered | size=340x60
SCORE VALUE     | top-Y=310px | x=centered | size=280x48
BEST LABEL      | top-Y=366px | x=centered | size=220x36
PLAY AGAIN      | top-Y=530px | x=centered | size=260x56
MAIN MENU       | top-Y=604px | x=centered | size=260x48
SHARE           | top-Y=668px | x=centered | size=180x44
```

---

### PauseScreen

**A) BACKGROUND IMAGE**

Game-field dot-grid background at full color but with a centered radial dark overlay (`#000000` at 65% opacity from Y=170 to Y=720, soft edge) creating a spotlight-dimmed stage effect. A single large centered card frame — rounded rectangle 360×420px at X=60, Y=217 — outlined in `#00FFB2` at 2px, inner glow `#00FFB2` 18px spread 25% opacity, hollow interior. Four small decorative corner accent marks in `#A259FF` (L-shaped, 16px) adorn each corner of the card frame. Top of canvas and bottom retain the same HUD strips from GameScreen (Y=0–80 and Y=800–854) at the same dimmed opacity, maintaining screen-layout consistency. A soft pulsing circular aura (`#00FFB2`, 120px radius, 15% opacity) centers behind the card frame, suggesting the game is paused but alive.

**B) BUTTON LAYOUT**

```
PAUSED LABEL    | top-Y=240px | x=centered | size=260x52
RESUME          | top-Y=380px | x=centered | size=260x56
RESTART         | top-Y=450px | x=centered | size=260x48
MAIN MENU       | top-Y=516px | x=centered | size=260x48
SOUND TOGGLE    | top-Y=588px | x=centered | size=200x44
```

---

## 4. Export Checklist

```
- icon_512.png (512x512)
- ui/menu_screen.png (480x854)
- ui/game_screen.png (480x854)
- ui/game_over_screen.png (480x854)
- ui/pause_screen.png (480x854)
- feature_banner.png (1024x500)
```

---

## 5. Feature Banner — feature_banner.png (1024×500 landscape)

**Canvas:** 1024×500px landscape, full bleed.

**Environment / Scene:** A dramatic wide-angle top-down perspective of the game arena — a vast dark circuit-board grid (`#07091A` base with `#152040` grid lines) stretching to the horizon with slight perspective tilt. In the left two-thirds of the canvas, a long neon-green pixel snake (8–10 visible segments, each a distinct chunky square) winds dynamically across the grid in an S-curve, its body glowing intensely: tight white-core highlight on each segment, wide `#00FFB2` bloom (60px soft spread). The snake's head (right-facing, at horizontal center ≈X=480) is mid-collision with a large 3D-beveled crimson brick — the brick is mid-shatter, exploding into 15–20 fragment shards in `#FF4B4B` and `#A259FF` flying outward. A starburst shockwave ring (`#A259FF` → transparent, 200px radius) emanates from the impact. Three intact brick formations (`#FF4B4B`, slightly glowing edges) loom in the right background, creating a threat landscape. Golden pellets (`#FFD700`) dot the grid at three positions along the snake's trail, each with a soft warm halo.

**Title Text:** "BLOCK SNAKE" rendered in a tall chunky pixel-display font — large, dominant, positioned left-aligned starting at X=60, vertical center Y=200–300. Letters in solid `#00FFB2` with a 3px `#FFFFFF` stroke and a wide teal outer glow (40px spread). Below the title, a one-line subtitle in smaller pixel font: "SMASH YOUR WAY THROUGH" in `#FFD700` at 60% of title size.

**Lighting / Mood:** Strong atmospheric vignette darkening all four canvas edges. A dramatic under-lighting bloom from the snake's body casts green light spill across the surrounding grid cells. The overall mood is kinetic, dangerous, and retro-electrifying — communicating the core loop (snake + destruction) instantly.