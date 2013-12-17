
/**--------------------------------------------------------------
// les grandes parties de l'appli à decrire
//--------------------------------------------------------------
	1.  La map
		1.a. Noeud et rue (sens de circulation )
		1.b. Zoom et dezoom
		1.c. Livraisons et couleurs
		
	2. La tournée 
		2.a. expliquer les champs
		2.b. sur l entrepot pas de tournée
	
	3. Listes des livraisons
		3.a. decrire le texte
		3.b. tout est bindé
		
	4. Description d'une livraison
		4.a. decrire les champs, surtout schedule
		
	5. Description du formulaire d ajout
		5.a. list client / newCli
		5.b. adresse
		5.c. plage horaire
		
	6. Affichage special grace au menu

*/

//================================================================
//	NOMMENCLATURE
//  --> retour attendu de l appli
//  ** description/explication à faire (voir au dessus)
//  // commentaires génériques
//================================================================	


/**------------------------------------------------------------------------------------
//  La vraie demo
//------------------------------------------------------------------------------------
*/

1. présenter les boutons sans "cliquer"
//////Bob ?
	1. Présenter rapidement les boutons avec leur tooltip (passer au dessus d'eux un à un)
	2. Montrer la correspondance avec la barre de menu et évoquer les raccourcis clavier
			NE PAS FAIRE LA DEMO DE CNTL+Q, MEFIEZ VOUS DE CNTL +Z 
			
2. reproduire une séquence de création
	1. importer map
	//////Bob ou Guillaume ?
		1. cliquer sur bouton map
		2. naviguer jusqu'aux maps
		3.1 essayer des prendre un tournée
		3.2 prendre la 20*20
		** 1.a.
		** 1.b.			
		
	2. importer tournée
	//////Bob ou Guillaume ?
//rq: les tournée 10*10 peuvent marcher sur une 20*20 car parfois les noeud ont la meme ref
		1. cliquer sur bouton tournée
		2. naviguer jusqu'aux tournées
		3. prendre la 20*20 1
		** 1.c.
		** 2.a.
		
	3. presenter et selectionner dans la map a tout va
	//////Guillaume
//le but est de montrer quela selection est facile, 
//par plusieurs endroits et donne acces a tel ou tel bouton
		1. cliquer sur une livraison dans la map 
		** 3.a.
		2. cliquer sur une livraison dans la liste 
		** 3.b. 
		** 4.a.
		3. cliquer sur l entrepot
		** 2.b.
		4. cliquer sur un noeud vide 
		
	4. ajouter une livraison
	//////Aline ?
//malmener le formulaire et malmener la tournee ensuite
		1. cliquer sur le bouton add
		** 5.a.
		** 5.b.
		2. selectionner un client existant 
		3. chercher la merde 
			1. selectionner new
			2. entrer un client deja existant (309)
			3. cliquer sur ok
			--> erreur client deja existant 
		4. entrer un client qui n existe pas encore
			5. cliquer sur ok
			--> erreur scheduler ( begin et fin trop proches)
			** 5.c.
			6. entrer une heure abberrante 92h
			7. cliquer sur ok
			--> erreur scheduler ( erreur heure)
			6. entrer une plage deja prise 7h à 15h
			7. cliquer sur ok
			--> erreur scheduler ( tagada)
		8. entrer un vrai scheduler 12h à 13h
		9. cliquer sur ok 
		--> recalcul de tournée avec la new liv
		10. recliquer sur add
		11. remplir le schedule 12h à 13h
		16. cliquer sur ok
		
	5. montrer les retards
	//////Aline ?
//ajouter un noeud au client par defaut
		1. charger une liv serrée //TODO
		2. creer une liv sur plage ... //TODO
		
	6. annuler /refaire
	//////Martin
//penser a dire que ceux ci deselectionnent pour eviter des problemes d incoherences
		0. creer deux livraisons
		1. cliquer sur annuler
		--> liv suppr
		2. cliquer sur annuler
		--> 2e liv suppr
		3. cliquer sur refaire
		--> retour liv
		4. selectionner un noeud 
		5. cliquer sur refaire 
		--> retour 2e liv + deselection
		6. cliquer sur annuler
		--> suppr liv
		
	7. supprimer
	//////Martin
		1. selectionner le premier noeud ajouté
		2. cliquer sur supprimer
		--> on ne peut plus refaire 
		3. importer la tournée limite //TODO
		4. suprimer toutes les livraisons
		--> pas de bug
		
	8. export
		1. cliquer sur exporter
		2. entrer une valeur incorrecte si possible
		--> erreur
		3. entrer une valeur correcte
		4. *** hors appli *** constater l export
		
	8. impro pour les profs
			
	
			
		
			
			
			
			
				
	
		