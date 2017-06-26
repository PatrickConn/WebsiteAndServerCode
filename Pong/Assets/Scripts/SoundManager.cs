using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SoundManager : MonoBehaviour {

	// Holds the single instance of the SoundManager
	// that can be accessed from any other script
	public static SoundManager Instance = null;

	// Will hold the sound effects
	public AudioClip goalBloop;
	public AudioClip lossBuzz;
	public AudioClip hitPaddleBloop;
	public AudioClip winSound;
	public AudioClip wallBloop;

	// Refers to the AudioSource added to
	// SoundManager to play sound effects
	private AudioSource soundEffectAudio;

	// Use this for initialization
	void Start () {

		// This is a singleton that makes sure you only
		// ever have one SoundManager
		// If someone tries to create another destroy it
		if (Instance == null)
		{
			Instance = this;
		} else if(Instance != this)
		{
			Destroy(gameObject);
		}

		/* YOU ACTUALLY DON'T NEED THIS
        AudioSource[] sources = GetComponents<AudioSource>();
 
        foreach(AudioSource source in sources)
        {
            if(source.clip == null)
            {
                soundEffectAudio = source;
            }
        }
        */

		// Use this instead
		AudioSource theSource = GetComponent<AudioSource> ();
		soundEffectAudio = theSource;

	}

	// Any script can call this to play a sound effect
	public void PlayOneShot(AudioClip clip)
	{
		soundEffectAudio.PlayOneShot(clip);
	}
}
