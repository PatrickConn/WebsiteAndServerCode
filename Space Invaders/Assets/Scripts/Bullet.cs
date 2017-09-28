using UnityEngine;
using System.Collections;

public class Bullet : MonoBehaviour {

	public float speed = 30;

	private Rigidbody2D rigidBody;

	// Use this for initialization
	void Start () {
		rigidBody = GetComponent<Rigidbody2D> ();

		rigidBody.velocity = Vector2.up * speed;
	}
	
	void OnTriggerEnter2D(Collider2D col)
	{
		if (col.tag == "Wall") {
			Destroy(gameObject);
		}

	}

	void OnBecomeInvisible(){

		Destroy(gameObject);
	}
}
