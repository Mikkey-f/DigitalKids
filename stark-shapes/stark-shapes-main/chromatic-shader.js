// chromatic-shader.js
const ChromaticAberrationShader = {
    uniforms: {
      "tDiffuse": { value: null },
      "resolution": { value: new THREE.Vector2(1, 1) },
      "strength": { value: 0.5 }  // Strength of the chromatic aberration effect
    },
  
    vertexShader: /* glsl */`
      varying vec2 vUv;
      void main() {
        vUv = uv;
        gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
      }
    `,
  
        fragmentShader: /* glsl */`
      uniform sampler2D tDiffuse;
      uniform vec2 resolution;
      uniform float strength;
      varying vec2 vUv;
  
      void main() {
        // Calculate distance from center (0.5, 0.5)
        vec2 uv = vUv - 0.5;
        float dist = length(uv);
        
        // Apply a smooth falloff near the center
        // This creates a "neutral zone" with no chromatic aberration
        float factor = smoothstep(0.0, 0.4, dist);
        
        // Normalize direction vector
        vec2 direction = normalize(uv);
        
        // Scale the offset by both the distance factor and our strength parameter
        vec2 redOffset = direction * strength * factor * dist;
        vec2 greenOffset = direction * strength * 0.6 * factor * dist;
        vec2 blueOffset = direction * strength * 0.3 * factor * dist;
        
        // Sample each color channel with its offset
        float r = texture2D(tDiffuse, vUv - redOffset).r;
        float g = texture2D(tDiffuse, vUv - greenOffset).g;
        float b = texture2D(tDiffuse, vUv - blueOffset).b;
        
        gl_FragColor = vec4(r, g, b, 1.0);
      }
    `
  };